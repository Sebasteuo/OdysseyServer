package Sorts;

import java.util.Arrays;
/**
 * Clase encargada de realizar los distintos ordenamientos de la biblioteca
 * @author Sebastian Alba
 * @author David Pereira
 * @author Randall Mendez 
 */
public class Sort {
	
	/**
	 * Constructor de la clase
	 */
	public Sort() {
		
	}
	/**
	 * Se encarga de llamar al metodo que ejecuta el ordenamiento Quicksort
	 * @param arr
	 * @return array
	 */
	public String[] quickSort(String[] arr) {
		int arrSize = arr.length;
		return executeQuickSort(arr, 0, arrSize-1);
	}
	/**
	 * Se encarga de llamar al metodo que ejecuta el ordenamiento RadixSort
	 * @param arr
	 * @return array
	 */
	public String[] radixSort(String[] arr) {
		return executeRadixSort(arr, 'a', 'z');
	}
	/**
	 * Se encarga de llamar al metodo que ejecuta el ordenamiento BubbleSort
	 * @param arr
	 * @return array
	 */
	public String[] bubbleSort(String[] arr) {
		int arrLength = arr.length;
		return executeBubbleSort(arr, arrLength);
	}
	/**
	 * Funcion principal que ejecuta la logica del ordenamiento quicksort
	 * @param arr
	 * @param firstPos
	 * @param lastPos
	 * @return array
	 */
	private String[] executeQuickSort(String[] arr, int firstPos, int lastPos){
	     if (firstPos < lastPos){
	         int pi = partition(arr, firstPos, lastPos); // pi es el indice de particion, arr[pi] ya esta en su lugar correcto
	         executeQuickSort(arr, firstPos, pi-1);  // Recursivamente ordena los elementos
	         executeQuickSort(arr, pi+1, lastPos);   // y despues de la particion
	     }
	     
	     return arr;
	}
	/**
	 * Funcion principal que ejecuta la logica del ordenamiento RadixSort
	 * @param arr
	 * @param lower
	 * @param upper
	 * @return
	 */
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
    /**
     * Funcion principal que ejecuta la logica del ordenamiento Bubblesort
     * @param arr
     * @param length
     * @return
     */
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
    
    /**
     * Funcion auxiliar del quicksort, se encarga de realizar las divisiones y ordenar ambas partes de la division binaria
     * @param arr
     * @param firstPos
     * @param lastPos
     * @return int
     */
	private int partition(String[] arr, int firstPos, int lastPos){ // Funcion aux del QuickSort
	     String pivot = arr[lastPos]; 
	     int i = (firstPos-1); // indice del elemento mas pequeno 
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
    /**
     * Funcion auxiliar del RadixSort, se encarga de tomar uno a uno los elementos del string, desde la posicion menos significativa a la mas significativa
     * e intercambia si uno se sale
     * @param arr
     * @param index
     * @param lower
     * @param upper
     */
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
