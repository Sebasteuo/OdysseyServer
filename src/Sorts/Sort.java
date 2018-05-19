package Sorts;

import java.util.Arrays;

public class Sort {
	
	public Sort() {
		
	}
	
	public String[] quickSort(String[] arr) {
		int arrSize = arr.length;
		return executeQuickSort(arr, 0, arrSize-1);
	}
	
	public String[] radixSort(String[] arr) {
		return executeRadixSort(arr, 'a', 'z');
	}
	
	public String[] bubbleSort(String[] arr) {
		int arrLength = arr.length;
		return executeBubbleSort(arr, arrLength);
	}
	
	private String[] executeQuickSort(String[] arr, int firstPos, int lastPos){
	     if (firstPos < lastPos){
	         int pi = partition(arr, firstPos, lastPos); // pi es el indice de particion, arr[pi] ya esta en su lugar correcto
	         executeQuickSort(arr, firstPos, pi-1);  // Recursivamente ordena los elementos
	         executeQuickSort(arr, pi+1, lastPos);   // y despues de la particion
	     }
	     
	     return arr;
	}
		  
    private String[] executeRadixSort(String[] arr,char lower,char upper){
         int maxIndex = 0;
         for(int i=0;i<arr.length;i++){
            if(arr[i].length()-1 > maxIndex){
            	maxIndex = arr[i].length()-1;
            }
         }
    
         for(int i=maxIndex;i>=0;i--){
        	 countingSort(arr,i,lower,upper);       
         }
         return arr;
    }
    
    private String[] executeBubbleSort(String[] arr, int length) {
    	String temp = "";
    	for (int i = 1; i < arr.length; i++) {
			for (int j = 0; j < arr.length-1; j++) {
				if (arr[j].compareToIgnoreCase(arr[j+1]) > 0) {
					temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
				}
			}
		}
    	return arr;
    }
    
	/* Estabece el pivot y organiza los elementos mas 
	 * pequeños que él a su izquierda y los mas grandes
	 * que él a su derecha */
	private int partition(String[] arr, int firstPos, int lastPos){ // Funcion aux del QuickSort
	     String pivot = arr[lastPos]; 
	     int i = (firstPos-1); // indice del elemento mas pequeño 
	     for (int j=firstPos; j<lastPos; j++){
	         if (arr[j].compareToIgnoreCase(pivot) <= 0){   // Si el elemento actual es menor
	             i++;			     // o igual al pivot
	             String temp = arr[i];  // intercambiar arr[i] y arr[j]
	             arr[i] = arr[j];
	             arr[j] = temp;
	         }
	     }
	
	     String temp = arr[i+1];  // intercambia arr[i+1] y arr[lastPos] (o el pivot)
	     arr[i+1] = arr[lastPos];
	     arr[lastPos] = temp;
	
	     return i+1;
	}
    
	private void countingSort(String[] arr,int index,char lower,char upper){ // Funcion aux del RadixSort
		int[] countArray = new int[(upper-lower)+2];
		String[] tempArray = new String[arr.length];
		Arrays.fill(countArray,0);
	    
		//Aumenta el contador del char en el indice
		for(int i=0;i<arr.length;i++){
			int charIndex = (arr[i].replace(" ", "").toLowerCase().length()-1 < index) ? 0 : (arr[i].replace(" ", "").toLowerCase().charAt(index) - lower)+1;
			countArray[charIndex]++;
		}
	
		for(int i=1;i<countArray.length;i++){
			countArray[i] += countArray[i-1];
		}
	    
		for(int i=arr.length-1;i>=0;i--){
			int charIndex = (arr[i].replace(" ", "").toLowerCase().length()-1 < index) ? 0 : (arr[i].replace(" ", "").toLowerCase().charAt(index) - lower)+1;
			tempArray[countArray[charIndex]-1] = arr[i];
			countArray[charIndex]--;
		}
	    
		for(int i=0;i<tempArray.length;i++){
			arr[i] = tempArray[i];
		}
	}
}
