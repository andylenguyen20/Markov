import java.util.ArrayList;

import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

public class EfficientWordMarkov implements MarkovInterface<WordGram> {
	private static final int DEFAULT_ORDER = 3;
	private String[] myText;
	private Random myRandom;
	private int myOrder;
	
	private static String PSEUDO_EOS = "";
	private static long RANDOM_SEED = 1234;
	
	private HashMap<WordGram, ArrayList<String>> mapFollows;
	
	public EfficientWordMarkov(int order){
		myRandom = new Random(RANDOM_SEED);
		myOrder = order;
	}
	public EfficientWordMarkov() {
		this(DEFAULT_ORDER);
	}
	@Override
	public void setTraining(String text) {
		// TODO Auto-generated method stub
		myText = text.split("\\s+");
		mapFollows = generateStructure();
	}
	public int size() {
		return myText.length;
	}
	
	public HashMap<WordGram, ArrayList<String>> generateStructure(){
		mapFollows = new HashMap<>();
		if(myText.length!=myOrder){
			for(int k=0; k < myText.length - myOrder - 1; k++){
				WordGram key = new WordGram(myText, k, myOrder);
				String follows = myText[k+myOrder];
				if(!mapFollows.containsKey(key)){
					mapFollows.put(key, new ArrayList<String>());
					mapFollows.get(key).add(follows);
				}
				else if(mapFollows.containsKey(key)){
				mapFollows.get(key).add(follows);
				}
			}
			WordGram lastKey = new WordGram(myText,myText.length-myOrder-1,myOrder);
			if(!mapFollows.containsKey(lastKey)){
				mapFollows.put(lastKey, new ArrayList<String>());
				mapFollows.get(lastKey).add(PSEUDO_EOS);
			}
			else if(mapFollows.containsKey(lastKey)){
				mapFollows.get(lastKey).add(PSEUDO_EOS);
			}
		}else{
			WordGram lastKey = new WordGram(myText,myText.length -myOrder - 1,myOrder);
			mapFollows.put(lastKey, new ArrayList<String>());
			mapFollows.get(lastKey).add(PSEUDO_EOS);
		}
		return mapFollows;
	}
	
	@Override
	public String getRandomText(int length) {
		StringBuilder sb = new StringBuilder();
		int index = myRandom.nextInt(myText.length - myOrder);
		WordGram current = new WordGram(myText, index, myOrder);
		sb.append(current.toString().replaceAll(",",""));
		//System.out.printf("first random %d for '%s'\n",index,current);
		for(int k=0; k < length-myOrder; k++){
			ArrayList<String> follows = getFollows(current);
			if (follows.size() == 0){
				break;
			}
			index = myRandom.nextInt(follows.size());
			
			String nextItem = follows.get(index);
			if (nextItem.equals(PSEUDO_EOS)) {
				//System.out.println("PSEUDO");
				break;
			}
			sb.append(" " + nextItem);
			current = current.shiftAdd(nextItem);
		}
		return sb.toString();
	}

	@Override
	public ArrayList<String> getFollows(WordGram key) {
		// TODO Auto-generated method stub
		return mapFollows.get(key);
	}
	
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return myOrder;
	}

	@Override
	public void setSeed(long seed) {
		// TODO Auto-generated method stub
		RANDOM_SEED = seed;
		myRandom = new Random(RANDOM_SEED);	
	}


}
