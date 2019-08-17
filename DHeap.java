package test;
import java.util.NoSuchElementException;
	//332305747  vladyslavk ,311453740  matanmatitya		
/**
 * D-Heap
 */
/*
 * TODO List: int getSize()VLAD //V// 
 * int arrayToHeap(DHeap_Item[] array1)Vlad
 * public boolean isHeap()MATAN //V - check if correct //
 * int Insert(DHeap_Item item) //V//VLAD 
 * int Decrease_Key(DHeap_Item item, int delta)MATAN //V - check if correct //
 * static int DHeapSort(int[] array1, int d) MATAN +
 * together heapifyUp+heapifyDown//V//
 */
public class DHeap {

	private int size, max_size, d;
	private DHeap_Item[] array;

	// Constructor
	// m_d >= 2, m_size > 0
	DHeap(int m_d, int m_size) {
		max_size = m_size;
		d = m_d;
		array = new DHeap_Item[max_size];
		size = 0;
	}

	/**
	 * public int getSize() Returns the number of elements in the heap.
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * public int arrayToHeap()
	 *
	 * The function builds a new heap from the given array. Previous data of the
	 * heap should be erased. preconidtion: array1.length() <= max_size
	 * postcondition: isHeap() size = array.length() Returns number of
	 * comparisons along the function run.
	 */
    public int arrayToHeap(DHeap_Item[] array1) {
    	int n=array1.length;
    	if ( n== 0){
    		return n; 
    	}
    	this.size = n;
    	this.array = new DHeap_Item[max_size];
    	for (int index = 0; index<n;index++){
    		this.array[index] = array1[index];
    		this.array[index].setPos(index);
    	}    
    	int count = 0;
    	int fnl = (int)((this.size-1)/this.d);
     	for (int index = fnl; index>=0; index--){
 		count = count + heapifyDown (array1[index]); 
    	}    	
        return count;
    }

	/**
	 * public boolean isHeap()
	 *
	 * The function returns true if and only if the D-ary tree rooted at
	 * array[0] satisfies the heap property or has size == 0.
	 * 
	 */
	public boolean isHeap() {
		if (this.array == null) {
			return false;
		}
		return true;
		// just for illustration - should be replaced by student
						// code
	}

	public boolean isHeapTree(int i) {  // check if correct
		if (i == this.array.length - 1) {
			return true;
		}		
		int j=0;
		while (j<this.d){
			if (this.array[i].getKey() >= (this.array[child(i+1,j,this.d)].getKey())) {
				return isHeapTree(child(i+1,j++,this.d));
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * public static int parent(i,d), child(i,k,d) (2 methods)
	 *
	 * precondition: i >= 0, d >= 2, 1 <= k <= d
	 *
	 * The methods compute the index of the parent and the k-th child of vertex
	 * i in a complete D-ary tree stored in an array. Note that indices of
	 * arrays in Java start from 0.
	 */
	public	static  int parent(int i, int d) { // check if correct

		return (i - 1) / d;
	} ///// +++++// just for illustration - should be replaced by student code

	public  static int child(int i, int k,int d) {// check if correct
	
		return d * i + k;
	} /// ++++// just for illustration - should be replaced by student code

	/**
	 * public int Insert(DHeap_Item item)
	 *
	 * Inserts the given item to the heap. Returns number of comparisons during
	 * the insertion.
	 *
	 * precondition: item != null isHeap() size < max_size
	 * 
	 * postcondition: isHeap()
	 */
	   public int Insert(DHeap_Item item) {
	    	this.array[this.size] = item;
	    	item.setPos(this.size);
	    	this.size++; 
	    	return heapifyUp(item);
	    }

	/**
	 * public int Delete_Min()
	 *
	 * Deletes the minimum item in the heap. Returns the number of comparisons
	 * made during the deletion.
	 * 
	 * precondition: size > 0 isHeap()
	 * 
	 * postcondition: isHeap()
	 */
	 public int Delete_Min() {
	    	DHeap_Item item = this.array[size-1];
	    	this.array[0]=item;
	    	item.setPos(0);
	    	this.array[this.size-1] = null;
	    	this.size --;
	    	return heapifyDown(item);
	     	
	    } 

	/**
	 * public DHeap_Item Get_Min()
	 *
	 * Returns the minimum item in the heap.
	 *
	 * precondition: heapsize > 0 isHeap() size > 0
	 * 
	 * postcondition: isHeap()
	 */
	public DHeap_Item Get_Min() // check if correct
	{
		return this.array[0];// should be replaced by student code
	}
	
	
	
    public DHeap_Item min_value_of_children (DHeap_Item item){//check if correct?__
		int numberc = Children(item);
		int position = item.getPos();
		DHeap_Item minChild = this.array[child(position,1,this.d)];
		for (int k = 2; k<=numberc;k++){
			DHeap_Item tempChild = this.array[child(position,k,this.d)];
			if (tempChild.getKey() < minChild.getKey()){
				minChild = tempChild;
			}
		}
		return minChild;
	}

	/**
	 * public int Decrease_Key(DHeap_Item item, int delta)
	 *
	 * Decerases the key of the given item by delta. Returns number of
	 * comparisons made as a result of the decrease.
	 *
	 * precondition: item.pos < size; item != null isHeap()
	 * 
	 * postcondition: isHeap()
	 */
	 public int Decrease_Key(DHeap_Item item, int delta){
	    	if (delta < 0 ){
	    		return 0; 
	    	}
	    	int key = item.getKey();
	    	item.setKey(key-delta);
	    	return heapifyUp(item);
	    }

	/**
	 * public int Delete(DHeap_Item item)
	 *
	 * Deletes the given item from the heap. Returns number of comparisons
	 * during the deletion.
	 *
	 * precondition: item.pos < size; item != null isHeap()
	 * 
	 * postcondition: isHeap()
	 */
    public int Delete(DHeap_Item item) {
	int Minn  = this.Get_Min().getKey();
	int delta = item.getKey()-Minn;
	int counter = this.Decrease_Key(item, delta+1);
    	return counter + this.Delete_Min();
    }
    
    /**
     * 
     * @param item
     * @return amount of compress
     */
    
        public int heapifyDown(DHeap_Item item) {//check it
    	int count = 0;
    	int numberc = Children(item);
    	if (numberc == 0){
    		return 0; 
    	}
    	DHeap_Item minChild = min_value_of_children(item);
    	if (minChild.getKey() < item.getKey()){ 		
    			int dHeapPosition = item.getPos();
    			int minChildPoistion = minChild.getPos();    			
    			this.array[minChildPoistion] = item; 
    			this.array[dHeapPosition] = minChild;
    			item.setPos(minChildPoistion);
    			minChild.setPos(dHeapPosition);    			
        		return count + numberc + heapifyDown(item); 
    		}    		
    	return count + numberc; 
       
    }

	/*
	 * Sort the input array using heap-sort (build a heap, and perform n times:
	 * get-min, del-min). Sorting should be done using the DHeap, name of the
	 * items is irrelevant.
	 * 
	 * Returns the number of comparisons performed.
	 * 
	 * postcondition: array1 is sorted
	 */
  
	public static int DHeapSort(int[] array1, int d) {
		int n=array1.length;
		DHeap_Item[] Darray=new DHeap_Item[n]; 		
		for(int i=0;i<n;i++){ 
			Darray[i]=new DHeap_Item(""+array1[i],array1[i]);
		} 
		int counter=0;
		DHeap heap=new DHeap(d,n);
		counter=counter +heap.arrayToHeap(Darray);
		for(int j=0;j<n;j++){
			array1[j]=heap.Get_Min().getKey();
			counter = counter+heap.Delete_Min();
		}
		return counter;
	}
		/**
		 * 
		 * @param DHeap_Item item
		 * @return number of children
		 */
	public int Children (DHeap_Item item){
    	int index = item.getPos();
    	int number = 0;
    	for (int j = (this.d*index)+1; j<=(this.d*index)+this.d; j++){
    		if (j<this.size) { 
    			number++;
    		}
    	}
		return number;
	} 


	 public int heapifyUp(DHeap_Item item) {
	    	int counter = 0;
	    	if (item.getPos() == 0){ //the given item is the root
	    		return 0; 
	    	}
	    	
	    	int parentPos = parent(item.getPos(), this.d);
	    	DHeap_Item parent = this.array[parentPos];
	    	
	    	if (item.getKey() < parent.getKey()){ 	    		
	    		int pp = parent.getPos();
	    		int itemp = item.getPos();
	    		this.array[pp] = item; 	
	    		this.array[itemp] = parent;	    		    		
	    		item.setPos(pp);
	    		parent.setPos(itemp);	    		    		
	    		return counter +heapifyUp(item)+ 1 ;
	    	}
	    	return counter ++; 
	    }	
	 
/**
 * 
 * @param array1
 * @param i
 * @param j
 * swap between i and j
 */
	public static void exchange(DHeap_Item[] array1,int i, int j){
		DHeap_Item t=array1[i];
		array1[i]=array1[j];
		array1[j]=t; 
        }


    
    


}
