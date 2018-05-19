package Sorts;

public class Sort {
	
	public Sort() {
		
	}
	
	public String[] quickSort(String[] arr) {
		int arrSize = arr.length;
		return executeQuickSort(arr, 0, arrSize-1);
	}
	
	public String[] radixSort(String[] arr) {
		return arr;
	}
	
	private String[] executeQuickSort(String[] arr, int firstPos, int lastPos){
	     if (firstPos < lastPos){
	         /* pi is partitioning index, arr[pi] is 
	           now at right place */
	         int pi = partition(arr, firstPos, lastPos); // pi es el indice de particion, arr[pi] ya esta en su lugar correcto
	         executeQuickSort(arr, firstPos, pi-1);  // Recursivamente ordena los elementos
	         executeQuickSort(arr, pi+1, lastPos);   // y despues de la particion
	     }
	     
	     return arr;
	}

	/* Estabece el pivot y organiza los elementos mas 
	 * pequeños que él a su izquierda y los mas grandes
	 * que él a su derecha */
	private int partition(String[] arr, int firstPos, int lastPos){
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
	
	 
}
