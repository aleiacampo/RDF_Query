
import rita.wordnet.RiWordnet;

public class Database {

	String values[][];
	int rows;
	String predicatesDirectBestPos[][];
	String predicatesDirectEveryPos[][];
	String predicatesInirectBestPos[][];
	String predicatesInirectEveryPos[][];	
	

	
	Database(int DIM_DB){
		
		values = new String[DIM_DB][3];
		rows = DIM_DB;
		int i, j;
		for(i=0; i<DIM_DB; i++)
			for(j=0; j<3; j++)
				values[i][j]="";
	}
	
	
	
	public void printTriple(int idTriple){
		
		int i;
		System.out.print("		");
		for(i=0; i<3; i++){
			System.out.print(values[idTriple][i]);
			if(i!=2) System.out.print(" | ");
		}
		System.out.println();
	}
	
	public void fillDb(String file){
		
		int i = 0, j = 0, countTerms = 0;
		
		predicatesDirectBestPos = new String[rows][];
		predicatesDirectEveryPos = new String[rows][];
		predicatesInirectBestPos = new String[rows][];
		predicatesInirectEveryPos = new String[rows][];
		
		file = file.replaceAll("\r\n|\r|\n","");
		String terms[] = file.split("	");		// tutte le parole del db in un array
		
		for(i = 0; i<rows; i++)
			for(j=0; j<3; j++){
				values[i][j] = terms[countTerms].toLowerCase().trim();
				countTerms++;
			}
		
		for(i = 0; i<rows; i++){
			predicatesDirectBestPos[i] = getSynonimsDbValues(values[i][1], false, true);
			predicatesDirectEveryPos[i] = getSynonimsDbValues(values[i][1], false, false);
			predicatesInirectBestPos[i] = getSynonimsDbValues(values[i][1], true, true);
			predicatesInirectEveryPos[i] = getSynonimsDbValues(values[i][1], true, false);
		}
		
		/*
		for(i = 0; i < rows; i ++){
			System.out.println();
			for(j = 0; j < predicatesInirectEveryPos[i].length; j ++){
				System.out.print(predicatesInirectEveryPos[i][j]+", ");
			}
		}
		*/
	}
	
	public static String[] getSynonimsDbValues(String word, boolean intensity, boolean best_pos){
		
		int i;
		int numberPos = 0;
		
	  	MyPrintStream myStream = new MyPrintStream(System.out);	// do il comando allo standard output alla mia classe
		System.setOut(myStream);
		
		RiWordnet wordnet = new RiWordnet();
		
		String[] getAllAntonyms = null, getAllSynonyms = null, getAllSimilar = null, getAllCoordinates = null, getAllHyponyms = null, getAllHypernyms = null, getStems = null, getAllSynsets = null, getAllNominalizations = null, getAllMeronyms = null, getAllDerivedTerms = null, getAllVerbGroups = null;
		String[] pos, synonyms = null;
		String[] synonymsV = null, synonymsN = null, synonymsA = null, synonymsR = null;
		
		pos = wordnet.getPos(word);
		numberPos = pos.length;
		if(best_pos){
			pos[0] = wordnet.getBestPos(word);
			numberPos = 1;
		}
		
		if(!intensity){	// sinonimi diretti
				for (i = 0; i < numberPos; i++){
					switch(pos[i]){
						case RiWordnet.VERB :
							getStems = wordnet.getStems(word, pos[i]);
							getAllSynsets = wordnet.getAllSynsets(word, pos[i]);
							getAllNominalizations = wordnet.getAllNominalizations(word, pos[i]);
							synonymsV = assemblyStrings(getStems, getAllSynsets, getAllNominalizations, null);
						break;
						case RiWordnet.NOUN :
							getAllMeronyms = wordnet.getAllMeronyms(word, pos[i]);
							getAllSynsets = wordnet.getAllSynsets(word, pos[i]);
							getAllNominalizations = wordnet.getAllNominalizations(word, pos[i]);
							synonymsN = assemblyStrings(getAllSynsets, getAllNominalizations, getAllMeronyms, null);
						break;
						case RiWordnet.ADJ :  
							getStems = wordnet.getStems(word, pos[i]);
							getAllSynsets = wordnet.getAllSynsets(word, pos[i]);
							synonymsA = assemblyStrings(getStems, getAllSynsets, null, null);
						break;
						case RiWordnet.ADV :
							getAllSynsets = wordnet.getAllSynsets(word, pos[i]);
							getAllDerivedTerms = wordnet.getAllDerivedTerms(word, pos[i]);
							synonymsR = assemblyStrings(getAllSynsets, getAllDerivedTerms, null, null);
						break;
						default : return getAllSynsets = wordnet.getAllSynsets(word, pos[i]);
					}
				}
		}
		else{	// sinonimi indiretti
			for (i = 0; i < numberPos; i++){
				switch(pos[i]){
					case RiWordnet.VERB :
						getAllVerbGroups = wordnet.getAllVerbGroups(word, pos[i]);
						getAllHypernyms = wordnet.getAllHypernyms(word, pos[i]);
						synonymsV = assemblyStrings(getAllVerbGroups, getAllHypernyms, null, null);
					break;
					case RiWordnet.NOUN :
						getAllHyponyms = wordnet.getAllHyponyms(word, pos[i]);
						getAllCoordinates = wordnet.getAllCoordinates(word, pos[i]);
						synonymsN = assemblyStrings(getAllHyponyms, getAllCoordinates, null, null);
					break;
					case RiWordnet.ADJ :  
						getAllNominalizations = wordnet.getAllNominalizations(word, pos[i]);
						getAllSimilar = wordnet.getAllSimilar(word, pos[i]);
						synonymsA = assemblyStrings(getAllNominalizations, getAllSimilar, null, null);
					break;
					case RiWordnet.ADV :
						getAllSynonyms = wordnet.getAllSynonyms(word, pos[i]);
						getAllAntonyms = wordnet.getAllAntonyms(word, pos[i]);
						synonymsR = assemblyStrings(getAllSynonyms, getAllAntonyms, null, null);
					break;
					default : return getAllSynsets = wordnet.getAllSynsets(word, pos[i]);
				}
			}
	}
		synonyms = assemblyStrings(synonymsV, synonymsN, synonymsA, synonymsR);
		return synonyms;
	}
	
	public static String[] assemblyStrings(String[] str1, String[] str2, String[] str3, String[] str4){
		
		int dim = 0, j, k = 0;
		String[] finalString;
		
		if(str1 != null) dim = dim+str1.length;
		if(str2 != null) dim = dim+str2.length;
		if(str3 != null) dim = dim+str3.length;
		if(str4 != null) dim = dim+str4.length;
		
		finalString = new String[dim];
		
		if(str1 != null)
			for(j=0; j<str1.length; j++)
				finalString[k++] = str1[j];
		if(str2 != null)
			for(j=0; j<str2.length; j++)
				finalString[k++] = str2[j];
		if(str3 != null)
			for(j=0; j<str3.length; j++)
				finalString[k++] = str3[j];
		if(str4 != null)
			for(j=0; j<str4.length; j++)
				finalString[k++] = str4[j];
		
		return finalString;
	}
	
}
