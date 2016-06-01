

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import rita.wordnet.*;

public class TestingRiWordnet {

	public static void print_words(String what, String[] str){
		
		int i;
		System.out.print("	"+what+":\n		");
		if(str != null)
		for(i = 0; i < str.length; i++)
			System.out.print(str[i]+", ");
		System.out.print("\n");
	}

	public static void main (String[] args) {
	
			int i = 0, j = 0, k = 0;
			
		  	InputStreamReader reader = new InputStreamReader(System.in);
			BufferedReader myInput = new BufferedReader(reader);
			String str= new String();
		  	System.out.println("Lemma:");
		  	System.out.print("  ");
		  	try { 
				str = myInput.readLine(); 
			} 
			catch (IOException e) {
				System.out.println ("Si è verificato un errore: " + e);
				System.exit(-1); 
			}
			
			
			
			
		RiWordnet wordnet = new RiWordnet();			
		if(wordnet.exists(str)){

			int number_pos = 0;
			String[] pos = wordnet.getPos(str);
			number_pos = pos.length;

				pos[0] = wordnet.getBestPos(str);

			for (i = 0; i < pos.length; i++){
				System.out.println("\npos "+pos[i]+" di "+str);
				
			  	String[] getAllAlsoSees = wordnet.getAllAlsoSees(str,pos[i]);
			  	String[] getAllAntonyms = wordnet.getAllSynonyms(str,pos[i]);  	
			  	String[] getAllCoordinates = wordnet.getAllCoordinates(str,pos[i]);
			  	String[] getAllDerivedTerms = wordnet.getAllDerivedTerms(str,pos[i]);
			  	String[] getAllHolonyms = wordnet.getAllHolonyms(str,pos[i]);
			  	String[] getAllHypernyms = wordnet.getAllHypernyms(str,pos[i]);
			  	String[] getAllHyponyms = wordnet.getAllHyponyms(str,pos[i]);
			  	String[] getAllMeronyms = wordnet.getAllMeronyms(str,pos[i]);
			  	String[] getAllNominalizations = wordnet.getAllNominalizations(str,pos[i]);
			  	String[] getAllSimilar = wordnet.getAllSimilar(str,pos[i]);
			  	String[] getAllSynonyms = wordnet.getAllSynonyms(str,pos[i]);
			  	String[] getAllSynsets = wordnet.getAllSynsets(str,pos[i]);
			  	String[] getAllVerbGroups = wordnet.getAllVerbGroups(str,pos[i]);	  	
			  	String[] getAlsoSees = wordnet.getAlsoSees(str,pos[i]);
			  	String[] getAnagrams = wordnet.getAnagrams(str,pos[i]);
			  	String[] getAntonyms = wordnet.getAntonyms(str,pos[i]);
			  	String[] getContains = wordnet.getContains(str,pos[i]);
			  	String[] getCoordinates = wordnet.getCoordinates(str,pos[i]);
			  	String[] getDerivedTerms = wordnet.getDerivedTerms(str,pos[i]);
			  	String[] getEndsWith = wordnet.getEndsWith(str,pos[i]);	  	
			  	String[] getHolonyms = wordnet.getHolonyms(str,pos[i]);
			  	String[] getHypernyms = wordnet.getHypernyms(str,pos[i]);
			  	String[] getHyponyms = wordnet.getHyponyms(str,pos[i]);
			  	String[] getMeronyms = wordnet.getMeronyms(str,pos[i]);
			  	String[] getNominalizations = wordnet.getNominalizations(str,pos[i]);
			  	String[] getSimilar = wordnet.getSimilar(str,pos[i]);
			  	String[] getSoundsLike = wordnet.getSoundsLike(str,pos[i]);
			  	String[] getStartsWith = wordnet.getStartsWith(str,pos[i]);
			  	String[] getStems = wordnet.getStems(str,pos[i]);
			  	String[] getSynonyms = wordnet.getSynonyms(str,pos[i]);
			  	String[] getSynset = wordnet.getSynset(str,pos[i]);
			  	String[] getVerbGroup = wordnet.getVerbGroup(str,pos[i]);
			  	String[] getWildcardMatch = wordnet.getWildcardMatch(str,pos[i]);


			  	print_words("getAllAlsoSees ", getAllAlsoSees );
			  	print_words("getAllAntonyms ", getAllAntonyms );
			  	print_words("getAllCoordinates ", getAllCoordinates );
			  	print_words("getAllDerivedTerms ", getAllDerivedTerms );
			  	print_words("getAllHolonyms ", getAllHolonyms );
			  	print_words("getAllHypernyms ", getAllHypernyms );
			  	print_words("getAllHyponyms ", getAllHyponyms );
			  	print_words("getAllMeronyms ", getAllMeronyms );
			  	print_words("getAllNominalizations ", getAllNominalizations );
			  	print_words("getAllSimilar ", getAllSimilar );
			  	print_words("getAllSynonyms ", getAllSynonyms );	  	
			  	print_words("getAllSynsets ", getAllSynsets );
			  	print_words("getAllVerbGroups ", getAllVerbGroups );
			  	print_words("getAlsoSees  ", getAlsoSees  );
			  	print_words("getAnagrams  ", getAnagrams  );
			  	print_words("getAntonyms  ", getAntonyms  );
			  	print_words("getContains  ", getContains  );
			  	print_words("getCoordinates  ", getCoordinates  );
			  	print_words("getDerivedTerms  ", getDerivedTerms  );
			  	print_words("getEndsWith  ", getEndsWith  );	  
			  	print_words("getHolonyms  ", getHolonyms  );
			  	print_words("getHypernyms  ", getHypernyms  );
			  	print_words("getHyponyms  ", getHyponyms  );
			  	print_words("getMeronyms  ", getMeronyms  );
			  	print_words("getNominalizations  ", getNominalizations  );
			  	print_words("getSimilar  ", getSimilar  );
			  	print_words("getSoundsLike  ", getSoundsLike  );
			  	print_words("getStartsWith  ", getStartsWith  );	  	
			  	print_words("getStems  ", getStems  );
			  	print_words("getSynonyms  ", getSynonyms  );
			  	print_words("getSynset   ", getSynset   );
			  	print_words("getVerbGroup   ", getVerbGroup   );
			  	print_words("getWildcardMatch   ", getWildcardMatch   );  	
		}
		}
	}
}
	
	

