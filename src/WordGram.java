
public class WordGram implements Comparable<WordGram> {

	private String[] myWords;
	private int myHash;
	public WordGram(String[] words, int start, int size){
		myWords = new String[size];
		for(int i = 0; i < size; i++){
			myWords[i] = words[start + i];
		}
		
		myHash = hashCode();
	}
	public int hashCode(){
		int sum = 0;
	    for(int k = 0; k < myWords.length; k++) {
	        sum += sum * 100 + myWords[k].hashCode();
	    }
	    return sum;
	}
	public boolean equals(Object other){
		if (other == this) 
            return true;
		if(other == null || !(other instanceof WordGram)){
			return false;
		}
		WordGram wg = (WordGram) other;
		for(int i = 0; i < length(); i++){
			if(!myWords[i].equals(wg.myWords[i])){
				return false;
			}
		}
		return true;
	}
	@Override
	public int compareTo(WordGram o) {
		// TODO Auto-generated method stub
		if(o.hashCode() == 0) return 1;
		if(hashCode() == 0) return -1;
		int shorterLength;
		boolean shorter = false, longer = false;
		if(o.length() > length()){
			shorterLength = length();
			shorter = true;
		}else if(o.length() < length()){
			shorterLength = o.length();
			longer = true;
		}else{
			shorterLength = o.length();
		}
		for(int i = 0; i < shorterLength; i++){
			if(this.myWords[i].compareTo(myWords[i]) > 0){
				return 1;
			}else if(this.myWords[i].compareTo(myWords[i]) < 0){
				return -1;
			}
			if(i == shorterLength - 1 && shorter){
				return -1;
			}
			if(i == shorterLength - 1 && longer){
				return 1;
			}
		}
		return 0;
	}
	public int length(){
		return myWords.length;
	}
	public String toString(){
		String str = "{";
		for(int i = 0; i < myWords.length - 1; i++){
			str += myWords[i] + ", ";
		}
		if(length()==0){
			str+= "}";
		}else{
			str+= myWords[myWords.length - 1] + "}";
		}
		return str;
	}
	public WordGram shiftAdd(String last){
		if(length()==0){
			return new WordGram(new String[0], 0, length());
		}
		String[] words = new String[length()];
		for(int i = 0; i < length() - 1; i++){
			words[i] = myWords[i+1];
		}
		words[length() - 1] = last;
		return new WordGram(words, 0, length());
		
	}
	

}
