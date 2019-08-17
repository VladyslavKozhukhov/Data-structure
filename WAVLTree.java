import java.util.Arrays;
// matanmatitya 311453740 vladyslavk 332305747

public class WAVLTree {
	private WAVLNode root;
	private int size = 0;
	private WAVLNode mini, maxi;

	 /**
	   * public boolean empty()
	   *
	   * returns true if and only if the tree is empty
	   *
	   */
	public boolean empty() { // checks if the tree is empty
		return size == 0;
	}
	
	 /**
	   * public String search(int k)
	   *
	   * returns the info of an item with key k if it exists in the tree
	   * otherwise, returns null
	   */
	public String search(int k) // returns value of the item with key k (if
								// exits). o.w -> returns null
	{
		WAVLNode itemFound = searchNode(this.root, k);
		if (itemFound == null)
			return null;
		if (itemFound.key == k)
			return itemFound.val;
		return null;
	}

	public static WAVLNode searchNode(WAVLNode node, int key) // function to
																// find node
																// with key k,
																// or insertion
																// spot for new
																// node
	{
		if (node == null)
			return null;
		if (node.key == key)
			return node;
		else if (node.key < key) {
			if (node.rightNode != null)
				return searchNode(node.rightNode, key);
			else
				return node;
		} else {
			if (node.leftNode != null)
				return searchNode(node.leftNode, key);
			else
				return node;
		}
	}

	private void inMinMaxUpdate(WAVLNode node) // updates minimum and maximum
												// fields after insertion
	{
		if (node.getKey() < this.mini.getKey())
			this.mini = node;
		if (node.getKey() > this.maxi.getKey())
			this.maxi = node;
	}

	private void delMinMaxUpdate(WAVLNode node) { // updates minimum and maximum
													// fields after deletion
		if (size == 0) {
			this.mini = null;
			this.maxi = null;
		} else {
			if (node.getKey() == this.mini.getKey())
				this.mini = (node.getRight() == null ? node.getParent() : node.getRight());
			if (node.getKey() == this.maxi.getKey())
				this.maxi = (node.getLeft() == null ? node.getParent() : node.getLeft());
		}
	}
	
	/**
	   * public int insert(int k, String i)
	   *
	   * inserts an item with key k and info i to the WAVL tree.
	   * the tree must remain valid (keep its invariants).
	   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	   * returns -1 if an item with key k already exists in the tree.
	   */
	
	public int insert(int k, String i) { // insert node with key k and value i.
											// keeps the WAVL tree valid (after
											// rebalancing) + returns # of
											// rebalancing op's. returns -1 if
											// item with key exists.
		if (size == 0) {
			root = new WAVLNode(k, i, null);
			size = size + 1;
			this.mini = this.root;
			this.maxi = this.root;
			this.root.setSubtreeSize(0);
			return 0;
		}
		WAVLNode node = searchNode(root, k);
		if (node.key == k) {
			return -1;
		}
		size = size + 1;
		WAVLNode newNode = new WAVLNode(k, i, node);
		if (k < node.key) {
			node.setLeft(newNode);
		} else {
			node.setRight(newNode);
		}
		inMinMaxUpdate(newNode);
		
			int ret=insertRebalance(node);
			//UpdateSubTree(this.root);
			return ret;
	}

	public int insertRebalance(WAVLNode node) { // rebalance tree after
												// insertion. ret -> # of
												// rebalancing op's.
		if (node == null) {
			return 0;
		}
		int lRankDiff = node.getlDiff();
		int rRankDiff = node.getrDiff();
		if (lRankDiff > 0 && rRankDiff > 0) {
			return 0;
		}
		// promote scenario
		if ((lRankDiff == 0 && rRankDiff == 1) || (lRankDiff == 1 && rRankDiff == 0)) {
			node.promote();
			// fix parent and up.
			return 1 + insertRebalance(node.getParent());
		}
		// single rotation scenario
		if ((lRankDiff == 0 && node.getLeft().getlDiff() == 1) || (rRankDiff == 0 && node.getRight().getrDiff() == 1)) {
			WAVLNode childNode;
			if (lRankDiff == 0) {
				childNode = node.getLeft();
			} else {
				childNode = node.getRight();
			}
			rotate(childNode);
			node.demote();
			return 1;
		}
		// double rotation scenario
		if ((rRankDiff == 0 && node.getRight().getrDiff() == 2) || (lRankDiff == 0 && node.getLeft().getlDiff() == 2)) {
			WAVLNode childNode, grandChildNode;
			if (lRankDiff == 0) {
				childNode = node.getLeft();
				grandChildNode = childNode.getRight();
			} else {
				childNode = node.getRight();
				grandChildNode = childNode.getLeft();
			}
			node.demote();
			childNode.demote();
			rotate(grandChildNode);
			rotate(grandChildNode);
			grandChildNode.promote();
			return 2;
		}

		return -1;
	}
	
	 /**
	   * public int delete(int k)
	   *
	   * deletes an item with key k from the binary tree, if it is there;
	   * the tree must remain valid (keep its invariants).
	   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	   * returns -1 if an item with key k was not found in the tree.
	   */
	
	public int delete(int k) { // deletes item with key k, and keeps the tree
								// balanced. ret - > # rebalancing op's. ret - >
								// if items with key k not found.
		if (size == 0)
			return -1;
		WAVLNode node = searchNode(root, k);
		if (node.key == k) {
			size--;
			delMinMaxUpdate(node);
			if (node.rightNode == null && node.leftNode == null) {
				if (size == 0)
					this.root = null;
				else
					node.getParent().dcChild(node);
			} else if (node.rightNode == null)
				childSwap(node, node.leftNode);
			else if (node.leftNode == null)
				childSwap(node, node.rightNode);
			else {
				WAVLNode PredSucc = PredSuccFind(node);
				if (PredSucc.rank == 1) {
					// replaced has a child!
					WAVLNode childNode = PredSucc.getLeft() == null ? PredSucc.getRight() : PredSucc.getLeft();
					childSwap(PredSucc, childNode);
				} else {
					PredSucc.getParent().dcChild(PredSucc);
				}
				WAVLNode predSuccParent = PredSucc.getParent();
				childSwap(node, PredSucc);
				PredSucc.copyChildren(node);
				PredSucc.setRank(node.getRank());
				WAVLNode toFix = predSuccParent == node ? PredSucc : predSuccParent;
				if (predSuccParent.getlDiff() == predSuccParent.getrDiff() && predSuccParent.getlDiff() == 3) {
					predSuccParent.demote();
					int ret= 1 + deleteRebalance(toFix);
					//UpdateSubTree(this.root);
					return ret;
				}
				int ret =deleteRebalance(toFix);
				//UpdateSubTree(this.root);
				return ret;
			}
		} else {	
			//UpdateSubTree(this.root);
			return -1;
		}
		int ret =deleteRebalance(node.getParent());
		//UpdateSubTree(this.root);
		return ret;
		

	}

	private int deleteRebalance(WAVLNode node) { // rebalance tree after
													// deleteion. ret -> # of
													// rebalancing op's.
		if (node == null) {
			return 0;
		}
		int lRankDiff = node.getlDiff();
		int rRankDiff = node.getrDiff();
		if (lRankDiff < 3 && rRankDiff < 3) {
			if (node.isRealNode() && node.getRank() == 1) {
				node.demote();
				return 1 + deleteRebalance(node.getParent());
			}
			return 0;
		}
		// demote scenario
		if ((lRankDiff == 3 && rRankDiff == 2) || (lRankDiff == 2 && rRankDiff == 3)) {
			node.demote();
			return 1 + deleteRebalance(node.getParent());
		}

		if (lRankDiff == 3 && rRankDiff == 1) {
			// double demote scenario
			if ((node.getRight().getrDiff() == 2) && (node.getRight().getlDiff() == 2)) {
				node.demote();
				node.getRight().demote();
				return 1 + deleteRebalance(node.getParent());
			}
			// single rotation scenario
			if (((node.getRight().getrDiff() == 1))) {
				WAVLNode childNode = node.getRight();
				rotate(childNode);
				childNode.promote();
				node.demote();

				if (node.isRealNode() && node.getRank() == 1)
					node.demote();

				return 1 + deleteRebalance(node.getParent());
			}
			// double rotation scenario
			if ((node.getRight().getrDiff() == 2) && (node.getRight().getlDiff() == 1)) {
				WAVLNode childNode = node.getRight();
				WAVLNode grandChildNode = childNode.getLeft();
				rotate(grandChildNode);
				rotate(grandChildNode);
				childNode.demote();
				grandChildNode.promote();
				grandChildNode.promote();
				node.demote();
				node.demote();
				return 2;
			}
		}
		if (lRankDiff == 1 && rRankDiff == 3) {
			// double demote scenario
			if ((node.getLeft().getlDiff() == 2) && (node.getLeft().getrDiff() == 2)) {
				node.demote();
				node.getLeft().demote();
				return 1 + deleteRebalance(node.getParent());
			}
			// single rotation scenario
			if (((node.getLeft().getlDiff() == 1))) {
				WAVLNode childNode = node.getLeft();
				rotate(childNode);
				childNode.promote();
				node.demote();

				if (node.isRealNode() && node.getRank() == 1)
					node.demote();

				return 1 + deleteRebalance(node.getParent());
			}
			// double rotation scenario
			if ((node.getLeft().getlDiff() == 2) && (node.getLeft().getrDiff() == 1)) {
				WAVLNode childNode = node.getLeft();
				WAVLNode grandChildNode = childNode.getRight();
				rotate(grandChildNode);
				rotate(grandChildNode);
				childNode.demote();
				grandChildNode.promote();
				grandChildNode.promote();
				node.demote();
				node.demote();
				return 2;
			}
		}

		return -1;
	}
	
	/**
	    * public String min()
	    *
	    * Returns the info of the item with the smallest key in the tree,
	    * or null if the tree is empty
	    */
	
	public String min() { // returns the tree minimum's value / null if empty
		return this.mini.val;
	}

	private WAVLNode miniNode(WAVLNode node) {// return smallest child of node.
		WAVLNode currentNode = node;
		while (currentNode.getLeft() != null) {
			currentNode = currentNode.getLeft();
		}
		return currentNode;
	}
	
	/**
	    * public String max()
	    *
	    * Returns the info of the item with the largest key in the tree,
	    * or null if the tree is empty
	    */
	
	public String max() { // returns the tree maximum's value / null if empty
		return this.maxi.val;
	}

	private WAVLNode maxiNode(WAVLNode node){	// return largest child of node.
		WAVLNode currentNode = node;
		while (currentNode.getRight() != null) {
			currentNode = currentNode.getRight();
		}
		return currentNode;
	}

	protected WAVLNode[] inOrder() { // returns in order(sorted) array of all nodes in the tree.
		if (size == 0) {
			return new WAVLNode[0];
		}
		WAVLNode[] inOrderWAVLTree = new WAVLNode[size];
		inOrder(root, 0, inOrderWAVLTree);
		return inOrderWAVLTree;
	}

	private int inOrder(WAVLNode node, int index, WAVLNode[] array) { // puts the tree nodes into array. inOrder - first do left, then node, then right.
		int inc = 0;
		if (node.leftNode != null) {
			inc += inOrder(node.leftNode, index, array);
		}
		array[index + inc] = node;
		inc++;
		if (node.rightNode != null) {
			inc += inOrder(node.rightNode, index + inc, array);
		}
		return inc;
	}

	 /**
	   * public int[] keysToArray()
	   *
	   * Returns a sorted array which contains all keys in the tree,
	   * or an empty array if the tree is empty.
	   */
	
	public int[] keysToArray() { //returns - sorted array that contains all keys in the tree
		int[] array = new int[size];
		WAVLNode[] nodesArr = inOrder();
		for (int i = 0; i < size; i++) {
			array[i] = nodesArr[i].getKey();
		}
		return array;
	}

	/**
	   * public String[] infoToArray()
	   *
	   * Returns an array which contains all info in the tree,
	   * sorted by their respective keys,
	   * or an empty array if the tree is empty.
	   */
	
	public String[] infoToArray() { //returns - sorted array that contains all values in the tree
		String[] array = new String[size];
		WAVLNode[] nodesArr = inOrder();
		for (int i = 0; i < size; i++) {
			array[i] = nodesArr[i].getValue();
		}
		return array;
	}

	 /**
	    * public int size()
	    *
	    * Returns the number of nodes in the tree.
	    *
	    * precondition: none
	    * postcondition: none
	    */
	
	public int size() { // ret - > # of nodes in tree
		return this.size;
	}

    /**
    * public int getRoot()
    *
    * Returns the root WAVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IWAVLNode getRoot()
   {
	   return this.root;
   }
   
	private void rotate(WAVLNode node) { // swap node with his parent without changing ranks.
		WAVLNode parentNode = node.getParent();
		childSwap(parentNode, node);

		if (parentNode.getRight() == node) {
			WAVLNode leftNode = node.getLeft();
			parentNode.setRight(leftNode);
			node.setLeft(parentNode);
		} else if (parentNode.getLeft() == node) {
			WAVLNode rightNode = node.getRight();
			parentNode.setLeft(rightNode);
			node.setRight(parentNode);
		}
	}
	
/**
 * 	
 * @param node.Update node subsize.
 */
public void UpdateSubTree(WAVLNode node){
		
		int counter=update(node);
		
}
	public static int update(WAVLNode node){
		if(node.getLeft()==null && node.getRight()==null){
			node.setSubtreeSize(0);
          return 1;
			
		}
      if(node==null)
        return 0;
    
		else{
			int sum=update(node.getLeft()) + update(node.getRight());
			
			node.setSubtreeSize(sum);
			//System.out.println(node.getSubtreeSize()+""+"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
          return 1+sum;
		}
	}
			


	private void childSwap(WAVLNode firstNode, WAVLNode secNode) { // secNode becomes firstNode's parent's child instead of firstNode
		WAVLNode parent = firstNode.getParent();
		if (parent == null) {
			// a is root, we will now have a new root!
			root = secNode;
			secNode.setParent(null);
			return;
		}
		// otherwise, check if a is right or left child.
		if (parent.getRight() == firstNode) {
			parent.setRight(secNode);
		} else {
			parent.setLeft(secNode);
		}
	}

	private WAVLNode PredSuccFind(WAVLNode n) { // finds the successor\Predecessor of a given node
		if (n.rightNode != null)
			return miniNode(n.getRight());

		return maxiNode(n.getLeft());
	}

	/**
	   * public interface IWAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IWAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public IWAVLNode getLeft(); //returns left child (if there is no left child return null)
		public IWAVLNode getRight(); //returns right child (if there is no right child return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)
		public int getSubtreeSize(); // Returns the number of real nodes in this node's subtree (Should be implemented in O(1))
	}
	
	/**
	 * public class WAVLNode 
	 *
	 * If you wish to implement classes other than WAVLTree (for example
	 * WAVLNode), do it in this file, not in another file. This is an example
	 * which can be deleted if no such classes are necessary.
	 */
	public static class WAVLNode implements IWAVLNode {
		private WAVLNode rightNode, leftNode, parentNode;
		private String val;
		private int key;
		private int rank;
		private int subtreeSize=0;
		/* protected int parentRankDiff; */

		WAVLNode(int key, String value, WAVLNode parent) {
			rightNode = null;
			leftNode = null;
			this.val = value;
			this.key = key;
			this.parentNode = parent;
			this.rank = 0;
		
		}

		// getters & setters
		public int getRank() {
			return this.rank;
		}

		public void setRank(int r) {
			this.rank = r;
		}

		public String getValue() {
			return this.val;
		}
		/**
		 * @return key if this node
		 */
		public int getKey() {return this.key;}
		/**
		 * @return left Node
		 */

		public WAVLNode getLeft() {return this.leftNode;}
		/**
		 * @return rightNode
		 */

		public WAVLNode getRight() {return this.rightNode;}
		/**
		 * 
		 * @return parentNode
		 */
		public WAVLNode getParent() {return this.parentNode;}

		/**
		 * Set the new parent of this node as parent.
		 * 
		 * @param parent
		 *            - the new parent.
		 */
		public void setParent(WAVLNode parent) {this.parentNode = parent;}

		/**
		 * @return the rank difference between this node to it's right child.
		 */
		public int getrDiff() {	if (this.rightNode == null) {return this.rank + 1;}return this.rank - this.rightNode.rank;}

		/**
		 * @return the rank difference between this node to it's left child.
		 */
		public int getlDiff() {	if (this.leftNode == null) {return this.rank + 1;}return this.rank - this.leftNode.rank;}
		/**
		 * update rank
		 */
		public void promote() {this.rank=rank+1;}
		/**
		 * update rank
		 */
		public void demote() {this.rank=rank-1;}

		/**
		 * Assigns new children to the replacement of the deleted node
		 * 
		 * @param old
		 *            - the old node
		 */
		public void copyChildren(WAVLNode old) {
			this.setRight(old.getRight());
			this.setLeft(old.getLeft());
		}

		/**
		 * Sets left child of this node as 'left'
		 * 
		 * @param left
		 *            - the new child
		 */
		public void setLeft(WAVLNode left) {
			if (left != null) {
				left.setParent(this);
			}
			this.leftNode = left;
		}

		/**
		 * Sets right child of this node as 'right'
		 * 
		 * @param right
		 *            - the new right child
		 */
		public void setRight(WAVLNode right) {
			if (right != null) {
				right.setParent(this);
			}
			this.rightNode = right;
		}

		/**
		 * if node is a child of this one, replace it with null.
		 * 
		 * @param node
		 */
		public void dcChild(WAVLNode node) {
			if (rightNode == node) {
				rightNode = null;
			} else if (leftNode == node) {
				leftNode = null;
			}
		}

		/**
		 * @return true if the node is a leaf, false else.
		 */
		public boolean isRealNode() {return ((leftNode == null) && (rightNode == null));}
		/**
		 * 
		 * @param i is a size of subtree
		 * 		 */
		public void setSubtreeSize(int i){
			this.subtreeSize=i;
		}
/**
 * @return subtreeSize
 */
		public int getSubtreeSize() {return this.subtreeSize;}
	}
	
	
	
}
