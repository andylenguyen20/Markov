import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class EfficientMarkov implements MarkovInterface<String> {

	private static final int DEFAULT_ORDER = 3;
	private String myText;
	private Random myRandom;
	private int myOrder;

	private static String PSEUDO_EOS = "";
	private static long RANDOM_SEED = 1234;

	private HashMap<String, ArrayList<String>> mapFollows;

	public EfficientMarkov(int order) {
		myRandom = new Random(RANDOM_SEED);
		myOrder = order;
	}

	public EfficientMarkov() {
		this(DEFAULT_ORDER);
	}

	@Override
	public void setTraining(String text) {
		// TODO Auto-generated method stub
		myText = text;
		mapFollows = generateStructure();
	}

	public int size() {
		return myText.length();
	}

	public String getRandomText(int length) {
		StringBuilder sb = new StringBuilder();
		if (myText.length() == myOrder) {
			return "";
		}
		int index = myRandom.nextInt(myText.length() - myOrder);

		String current = myText.substring(index, index + myOrder);
		// System.out.printf("first random %d for '%s'\n",index,current);
		sb.append(current);
		for (int k = 0; k < length - myOrder; k++) {
			ArrayList<String> follows = getFollows(current);
			if (follows.size() == 0) {
				break;
			}
			index = myRandom.nextInt(follows.size());
		
			String nextItem = follows.get(index);
			if (nextItem.equals(PSEUDO_EOS)) {
				// System.out.println("PSEUDO");
				break;
			}
			sb.append(nextItem);
			current = current.substring(1) + nextItem;
		}
		return sb.toString();
	}

	public HashMap<String, ArrayList<String>> generateStructure() {
		mapFollows = new HashMap<>();
		if (myText.length() != myOrder) {
			for(int k = 0; k < myText.length() - myOrder + 1; k++){
				String key = myText.substring(k,k+myOrder);
				if(!mapFollows.containsKey(key)){
					mapFollows.put(key, new ArrayList<String>());
				}
				ArrayList<String> list = mapFollows.get(key);
				
				if(k+myOrder>=myText.length()){
					list.add(PSEUDO_EOS);
				}
				if(k+myOrder<myText.length()){
					list.add("" + myText.charAt(k+myOrder));
				}
				mapFollows.put(key,list);
			}
			
		} else {
			String lastKey = myText;
			if (!mapFollows.containsKey(lastKey)) {
				mapFollows.put(lastKey, new ArrayList<String>());
				mapFollows.get(lastKey).add(PSEUDO_EOS);
			} else if (mapFollows.containsKey(lastKey)) {
				mapFollows.get(lastKey).add(PSEUDO_EOS);
			}
		}
		
		return mapFollows;
	}

	public ArrayList<String> getFollows(String key) {
		return mapFollows.get(key);
	}

	@Override
	public int getOrder() {
		return myOrder;
	}

	@Override
	public void setSeed(long seed) {
		RANDOM_SEED = seed;
		myRandom = new Random(RANDOM_SEED);
	}

}
