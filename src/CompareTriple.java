
import rita.wordnet.*;

import java.io.*;
import java.lang.String;
import java.nio.file.Files;
import java.nio.file.Paths;



public class CompareTriple {
	
	static boolean[] alreadyPrinted;
	
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
	public static String[] getPos(String str){

		RiWordnet wordnet = new RiWordnet();
		
		if(!wordnet.exists(str))
			return null;
		String[] getPos = wordnet.getPos(str);
		for(int i=0; i<getPos.length; i++)
			switch(getPos[i]){
				case RiWordnet.ADJ :  getPos[i] = "Aggettivo"; break;
				case RiWordnet.NOUN :  getPos[i] = "Sostantvo"; break;
				case RiWordnet.ADV :  getPos[i] = "Avverbio"; break;
				case RiWordnet.VERB :  getPos[i] = "Verbo"; break;
				default : getPos[i] = null;
			}
		return getPos;
	}
	public static boolean compareTerm(Term subject, Term predicate, Term complement, Database db, boolean esit, int intensity){
		
		int i = 0, j = 0, k = 0;
	  	int intQuery = 0;
	  	int intValueDb = 0;
	  	int compareString = 0;
	  	boolean esitIntConversion = false;
	  	String complementToCompare = null;
	  	
	  	int[] predicateSynonymsFound = new int[db.rows];
	  	boolean findSynonymsPredicare = false;
	  	
	  	switch(intensity){
	  		case 1 : 
	  			predicateSynonymsFound = predicate.predicateFoundInDb;
	  			findSynonymsPredicare = predicate.findPredicate;
			break;
			case 2 : 
				predicateSynonymsFound = predicate.predicateSynonymsFoundInDbDirectBestPos;
				findSynonymsPredicare = predicate.findDirectBestPos;
			break;
			case 3 : 
				predicateSynonymsFound = predicate.predicateSynonymsFoundInDbDirectEveryPos;
				findSynonymsPredicare = predicate.findDirectEveryPos;
			break;
			case 4 :
				predicateSynonymsFound = predicate.predicateSynonymsFoundInDbIndirectBestPos;
				findSynonymsPredicare = predicate.findIndirectBestPos;
			break;
			case 5 :
				predicateSynonymsFound = predicate.predicateSynonymsFoundInDbIndirectEveryPos;
				findSynonymsPredicare = predicate.findIndirectEveryPos;
			break;
			default :
				predicateSynonymsFound = predicate.predicateFoundInDb;
				findSynonymsPredicare = predicate.findPredicate;
	  	}
	  	/*
	  	System.out.println();
	  	for(i=0; i<db.rows; i++)
	  		System.out.print(predicati_trovati[i]+" ");
	  	*/
	  	
/** controllo se devo fare ricerche aperte o cercare una sola tripla **/
	  	if(complement.word.charAt(0)=='>' || complement.word.charAt(0)=='<' || complement.word.equals("?")){
		/** se trova un: soggetto  -> soggetto comunicato 
		***				 predicato -> predicato comunicato **/
			  		if(subject.findSubject && findSynonymsPredicare)
				  		for(i=0; i<db.rows; i++)
				  			for(j=0; j<db.rows; j++)
				  				if(subject.subjectsFoundInDb[i] == predicateSynonymsFound[j] && subject.subjectsFoundInDb[i] != -1){
				  					if(complement.word.equals("?") && !alreadyPrinted[subject.subjectsFoundInDb[i]]){
				  						db.printTriple(subject.subjectsFoundInDb[i]);
				  						alreadyPrinted[subject.subjectsFoundInDb[i]] = true;
				  						esit = true;
				  					//	for(k=0; k<db.predicati_diretti_best_pos[soggetto.soggetti_utili[i]].length; k++)
				  					//	System.out.println(db.predicati_diretti_best_pos[soggetto.soggetti_utili[i]][k]);
				  					}
				  					if(complement.word.charAt(0)=='>' || complement.word.charAt(0)=='<'){
				  						complementToCompare = complement.word.substring(1);	// tolgo dalla stringa il valore di confronto
				  						try {	// controllo che entrambi i termini di paragone siano numeri
				  							intQuery = Integer.valueOf(complementToCompare);
				  							intValueDb = Integer.valueOf(db.values[subject.subjectsFoundInDb[i]][2]);
				  							esitIntConversion = true;
				  						}
				  						catch (NumberFormatException e) { // in caso contrario confronterò in senso alfabetico
				  							compareString = complementToCompare.compareTo(db.values[subject.subjectsFoundInDb[i]][2]);
				  							esitIntConversion=false;
				  						}
				  						if(complement.word.charAt(0)=='>' && !alreadyPrinted[subject.subjectsFoundInDb[i]]){
				  				  			if(esitIntConversion && intValueDb > intQuery){ // se la stringa è maggiore in senso numerico
				  				  				db.printTriple(subject.subjectsFoundInDb[i]); 
				  				  				alreadyPrinted[subject.subjectsFoundInDb[i]] = true;
				  				  				esit = true; }
				  				  			if(!esitIntConversion && compareString > 0){	// se la stringa è maggiore in senso alfabetico
				  				  				db.printTriple(subject.subjectsFoundInDb[i]);
				  				  				alreadyPrinted[subject.subjectsFoundInDb[i]] = true;
				  				  				esit = true; }
				  				  		}
				  				  		if(complement.word.charAt(0)=='<' && !alreadyPrinted[subject.subjectsFoundInDb[i]]){
				  				  			if(esitIntConversion && intValueDb < intQuery){ // se la stringa è maggiore in senso numerico
				  				  				db.printTriple(subject.subjectsFoundInDb[i]);
				  				  				alreadyPrinted[subject.subjectsFoundInDb[i]] = true;
				  				  				esit = true; }
				  				  			if(!esitIntConversion && compareString < 0){	// se la stringa è maggiore in senso alfabetico
				  				  				db.printTriple(subject.subjectsFoundInDb[i]); 
				  				  				alreadyPrinted[subject.subjectsFoundInDb[i]] = true;
				  				  				esit = true; }
				  				  		}
				  					}
				  				}
		/** se trova un: complemento -> soggetto comunicato 
		***				 predicato   -> predicato comunicato **/
			  		if(subject.findComplement && findSynonymsPredicare){
			  			for(i=0; i<db.rows; i++)
			  				for(j=0; j<db.rows; j++)
			  					if(subject.complementsFoundInDb[i] == predicateSynonymsFound[j] && subject.complementsFoundInDb[i] != -1){
			  						if(complement.word.equals("?") && !alreadyPrinted[subject.complementsFoundInDb[i]]){	// metto qui perchè l'indice deve essere diverso da -1
			  							db.printTriple(subject.complementsFoundInDb[i]);
			  							alreadyPrinted[subject.complementsFoundInDb[i]] = true;
			  							esit = true;
			  						//	for(k=0; k<db.predicati_diretti_best_pos[subject.complementi_utili[i]].length; k++)
					  					//	System.out.println(db.predicati_diretti_best_pos[subject.complementi_utili[i]][k]);
					  					
			  						}
			  						if(complement.word.charAt(0)=='>' || complement.word.charAt(0)=='<'){
			  							complementToCompare = complement.word.substring(1);	// tolgo dalla stringa il valore di confronto
				  						try {	// controllo che entrambi i termini di paragone siano numeri
				  							intQuery = Integer.valueOf(complementToCompare);
				  							intValueDb = Integer.valueOf(db.values[subject.complementsFoundInDb[i]][0]);
				  							esitIntConversion = true;
				  						}
				  						catch (NumberFormatException e) { // in caso contrario confronterò in senso alfabetico
				  							compareString = complementToCompare.compareTo(db.values[subject.complementsFoundInDb[i]][0]);
				  							esitIntConversion=false;
				  						}
				  						if(complement.word.charAt(0)=='>'  && !alreadyPrinted[subject.complementsFoundInDb[i]]){
				  				  			if(esitIntConversion && intValueDb > intQuery){ // se la stringa è maggiore in senso numerico
				  				  				db.printTriple(subject.complementsFoundInDb[i]); 
				  				  				alreadyPrinted[subject.complementsFoundInDb[i]] = true;
				  				  				esit = true; }
				  				  			if(!esitIntConversion && compareString > 0){	// se la stringa è maggiore in senso alfabetico
				  				  				db.printTriple(subject.complementsFoundInDb[i]);
				  				  				alreadyPrinted[subject.complementsFoundInDb[i]] = true;
				  				  				esit = true; }
				  				  		}
				  				  		if(complement.word.charAt(0)=='<'  && !alreadyPrinted[subject.complementsFoundInDb[i]]){
				  				  			if(esitIntConversion && intValueDb < intQuery){ // se la stringa è maggiore in senso numerico
				  				  				db.printTriple(subject.complementsFoundInDb[i]);
				  				  				alreadyPrinted[subject.complementsFoundInDb[i]] = true;
				  				  				esit = true; }
				  				  			if(!esitIntConversion && compareString < 0){	// se la stringa è maggiore in senso alfabetico
				  				  				db.printTriple(subject.complementsFoundInDb[i]);
				  				  				alreadyPrinted[subject.complementsFoundInDb[i]] = true;
				  				  				esit = true; }
				  				  		}
				  					}
			  					}
			  		}
		/** se trova un: soggetto -> (complemento -> soggetto comunicato)
		***				 predicato   -> predicato comunicato 
		***				 predicato che lega le due tuple del db -> "verbo essere" **/
			  		if(subject.findComplement && findSynonymsPredicare)
			  			for(i=0; i<db.rows; i++)
			  			  	for(j=0; j<db.rows; j++)
			  			  				// devo prendere, dove c'è "è", i soggetti degli oggetti utili
			  			  		if(subject.complementsFoundInDb[i] != -1 && predicateSynonymsFound[j] != -1)
			  			  		if(db.values[subject.complementsFoundInDb[i]][1].equals("be") &&	// se i complementi sono legati dal verbo essere
			  			  		   db.values[subject.complementsFoundInDb[i]][0].equals(db.values[predicateSynonymsFound[j]][0]) &&
			  			  		   !alreadyPrinted[predicateSynonymsFound[j]]){
				  			  		if(complement.word.equals("?")){
				  			  			db.printTriple(predicateSynonymsFound[j]);
				  			  			alreadyPrinted[predicateSynonymsFound[j]] = true;
				  				  		esit = true;
				  				//  	for(k=0; k<db.predicati_diretti_best_pos[predicati_trovati[j]].length; k++)
				  					//	System.out.println(db.predicati_diretti_best_pos[predicati_trovati[j]][k]);
				  					
				  			  		}
				  			  		if(complement.word.charAt(0)=='>' || complement.word.charAt(0)=='<'){
				  			  		complementToCompare = complement.word.substring(1);	// tolgo dalla stringa il valore di confronto
				  			  			try {	// controllo che entrambi i termini di paragone siano numeri
				  			  				intQuery = Integer.valueOf(complementToCompare);
				  			  				intValueDb = Integer.valueOf(db.values[predicateSynonymsFound[j]][2]);
				  			  				esitIntConversion = true;
				  			  			}
				  			  			catch (NumberFormatException e) { // in caso contrario confronterò in senso alfabetico
				  			  				compareString = complementToCompare.compareTo(db.values[predicateSynonymsFound[j]][2]);
				  			  				esitIntConversion=false;
				  			  			}
				  			  	//	System.out.println("1: "+dato_da_comparare+" 2: "+db.valori[predicato.predicati_utili[j]][2]);
				  			  			if(complement.word.charAt(0)=='>'){
				  			  				if(esitIntConversion && intValueDb > intQuery){ // se la stringa è maggiore in senso numerico
				  			  					db.printTriple(predicateSynonymsFound[j]); 
				  			  					alreadyPrinted[predicateSynonymsFound[j]] = true;
				  			  					esit = true; } 
				  			  				if(!esitIntConversion && compareString > 0){	// se la stringa è maggiore in senso alfabetico
				  			  					db.printTriple(predicateSynonymsFound[j]);
				  			  					alreadyPrinted[predicateSynonymsFound[j]] = true;
				  			  					esit = true; }
				  			  			}
				  			  			if(complement.word.charAt(0)=='<'){
				  			  				if(esitIntConversion && intValueDb < intQuery){ // se la stringa è maggiore in senso numerico
				  			  					db.printTriple(predicateSynonymsFound[j]);
				  			  					alreadyPrinted[predicateSynonymsFound[j]] = true;
				  			  					esit = true; }
				  			  				if(!esitIntConversion && compareString < 0){	// se la stringa è maggiore in senso alfabetico
				  			  					db.printTriple(predicateSynonymsFound[j]);
				  			  					alreadyPrinted[predicateSynonymsFound[j]] = true;
				  			  					esit = true; }
				  			  			}
				  			  		}
			  			  		}
	  	}
		else{	// sta chiedendo se c'è la coincidenza esatta, cioè se esiste la tripla che do in ingresso nel db		  	  	
			for(i=0; i<db.rows; i++)
				for(j=0; j<db.rows; j++)
					for(k=0; k<db.rows; k++){
						if(subject.subjectsFoundInDb[i] == predicateSynonymsFound[j] && 
						   subject.subjectsFoundInDb[i] == complement.complementsFoundInDb[k] && 
						   subject.subjectsFoundInDb[i] != -1 && !alreadyPrinted[subject.subjectsFoundInDb[i]])
							if(!alreadyPrinted[subject.subjectsFoundInDb[i]]){
								db.printTriple(subject.subjectsFoundInDb[i]);	// basta stampare uno qualsiasi dei 3
								alreadyPrinted[subject.subjectsFoundInDb[i]] = true;
								esit = true;
							}
					}
		}
	  	return esit;
	}
	public static String[] getSynonyms(String predicate, boolean depth, boolean every_pos){

		int i;
		int numberPos = 0;
		

		RiWordnet wordnet = new RiWordnet();
		
		String[] getAllAntonyms = null, getAllSynonyms = null, getAllCoordinates = null, getAllHypernyms = null, getStems = null, getAllSynsets = null, getAllNominalizations = null, getAllMeronyms = null, getAllDerivedTerms = null, getAllVerbGroups = null;
		String[] pos, synonyms = null;
		String[] synonyms_v = null, synonyms_n = null, synonyms_a = null, synonyms_r = null;
		
		pos = wordnet.getPos(predicate);
		numberPos = pos.length;
		if(!every_pos){
			pos[0] = wordnet.getBestPos(predicate);
			numberPos = 1;
		}
		
		if(!depth){	// sinonimi diretti
				for (i = 0; i < numberPos; i++){
					switch(pos[i]){
						case RiWordnet.VERB :
							getStems = wordnet.getStems(predicate, pos[i]);
							getAllSynsets = wordnet.getAllSynsets(predicate, pos[i]);
							getAllNominalizations = wordnet.getAllNominalizations(predicate, pos[i]);
							synonyms_v = assemblyStrings(getStems, getAllSynsets, getAllNominalizations, null);
						break;
						case RiWordnet.NOUN :
							getAllMeronyms = wordnet.getAllMeronyms(predicate, pos[i]);
							getAllSynsets = wordnet.getAllSynsets(predicate, pos[i]);
							getAllNominalizations = wordnet.getAllNominalizations(predicate, pos[i]);
							synonyms_n = assemblyStrings(getAllSynsets, getAllNominalizations, getAllMeronyms, null);
						break;
						case RiWordnet.ADJ :  
							getStems = wordnet.getStems(predicate, pos[i]);
							getAllSynsets = wordnet.getAllSynsets(predicate, pos[i]);
							synonyms_a = assemblyStrings(getStems, getAllSynsets, null, null);
						break;
						case RiWordnet.ADV :
							getAllSynsets = wordnet.getAllSynsets(predicate, pos[i]);
							getAllDerivedTerms = wordnet.getAllDerivedTerms(predicate, pos[i]);
							synonyms_r = assemblyStrings(getAllSynsets, getAllDerivedTerms, null, null);
						break;
						default : return getAllSynsets = wordnet.getAllSynsets(predicate, pos[i]);
					}
				}
		}
		else{	// sinonimi indiretti
			for (i = 0; i < numberPos; i++){
				switch(pos[i]){
					case RiWordnet.VERB :
						getAllVerbGroups = wordnet.getAllVerbGroups(predicate, pos[i]);
						getAllHypernyms = wordnet.getAllHypernyms(predicate, pos[i]);
						getAllSynonyms = wordnet.getAllSynonyms(predicate, pos[i]);
						synonyms_v = assemblyStrings(getAllVerbGroups, getAllHypernyms, getAllSynonyms, null);
					break;
					case RiWordnet.NOUN :
						getAllSynonyms = wordnet.getAllSynonyms(predicate, pos[i]);
						getAllCoordinates = wordnet.getAllCoordinates(predicate, pos[i]);
						synonyms_n = assemblyStrings(getAllSynonyms, getAllCoordinates, null, null);
					break;
					case RiWordnet.ADJ :  
						getAllNominalizations = wordnet.getAllNominalizations(predicate, pos[i]);
						getAllSynonyms = wordnet.getAllSynonyms(predicate, pos[i]);
						synonyms_a = assemblyStrings(getAllNominalizations, getAllSynonyms, null, null);
					break;
					case RiWordnet.ADV :
						getAllSynonyms = wordnet.getAllSynonyms(predicate, pos[i]);
						getAllAntonyms = wordnet.getAllAntonyms(predicate, pos[i]);
						synonyms_r = assemblyStrings(getAllSynonyms, getAllAntonyms, null, null);
					break;
					default : return getAllSynsets = wordnet.getAllSynsets(predicate, pos[i]);
				}
			}
	}
		synonyms = assemblyStrings(synonyms_v, synonyms_n, synonyms_a, synonyms_r);
		return synonyms;
	}

	public static void main(String args[]) throws IOException{
		
	  	boolean tripleFound = false;
		int i;
	  	int stepSearch;
	  	int numberSynonymsFound;
	  	String[] inputTriple;
	  	String[] synonymsDirectBestPos;
	  	String[] synonymsDirectEveryPos;
	  	String[] synonymsIndirectBestPos;
	  	String[] synonymsIndirectEveryPos;
	  	boolean loop;
	  	int depth;
	  	boolean syntaxTriple;
	  	InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader myInput = new BufferedReader(reader);
		String str= new String();
		
/**__________________________
 Caricamento del database **/


		
		int count_line_db = 0;
		String path = "src/database.txt";	// metto tutto il contenuto del file in una stringa

		String db_string = new String(Files.readAllBytes(Paths.get(path)));

        FileReader reader_file = new FileReader(path);
		BufferedReader in = new BufferedReader(reader_file);
	    while(in.readLine() !=null) count_line_db++; 	    
	    
		Database db = new Database(count_line_db); 
		db.fillDb(db_string);

		for(i = 0; i<count_line_db; i++)
					db.printTriple(i);
		
		
		alreadyPrinted = new boolean[count_line_db];
		
		do{
		
			loop = false;
			tripleFound = false;
			stepSearch = 0;
			depth = 6;
		  	numberSynonymsFound = 0;
		  	synonymsDirectBestPos = null;
		  	synonymsDirectEveryPos = null;
		  	synonymsIndirectBestPos = null;
		  	synonymsIndirectEveryPos = null;
		  	inputTriple = new String[3];
				
			for(i = 0; i<count_line_db; i++)
				alreadyPrinted[i] = false;

/**____________________________________		
 Richiesta tripla input da tastiera **/
			
			do{
				inputTriple = null;
			  	syntaxTriple = false;
			  	System.out.println("Tripla di ricerca (per separare sono ammessi i caratteri '/' ',' '_' '-')");
			  	System.out.print("  ");
			  	try { 
					str = myInput.readLine(); 
				} 
				catch (IOException e) {
					System.out.println ("Si è verificato un errore: " + e);
					System.exit(-1); 
				}
				if(str.contains("/"))
					inputTriple = str.split("/");
				if(str.contains(","))
					inputTriple = str.split(",");
				if(str.contains("-"))
					inputTriple = str.split("-");
				if(str.contains("_"))
					inputTriple = str.split("_");
				try{
					inputTriple[0] = inputTriple[0];
					inputTriple[1] = inputTriple[1];
					inputTriple[2] = inputTriple[2];
				}
				catch(ArrayIndexOutOfBoundsException | NullPointerException e){
					syntaxTriple = true;
				}
			}
			while(syntaxTriple);
			
/**__________________________________________________
 Analisi e ricerca della tripla d'ingresso nel db **/
			
			Term subject = new Term(count_line_db, inputTriple[0].toLowerCase());
		  	Term complement = new Term(count_line_db, inputTriple[2].toLowerCase());
		  	Term[] predicates = new Term[1000];
		  	
		  	subject.searchWordsMatchInDb(db);
	  		complement.searchWordsMatchInDb(db);
	  	  	
	  	  	String[] getPos = getPos(inputTriple[1].toLowerCase());

	  	  	if(getPos == null){
	  	  		depth = 1;
	  	  		System.out.println("Non è stata trovata corrispondenza del predicato su WordNet");
	  	  	}

	  	  	while(stepSearch<depth && tripleFound==false){
	  	  		
	  	  		System.out.print("	"+(stepSearch+1)+". ");
	  	  		
	  	  		if(stepSearch==0){	/// primo tipo di ricerca, senza wordnet
	  	  			predicates[0] = new Term(count_line_db, inputTriple[1].toLowerCase());
	  	  			numberSynonymsFound=1;
	  	  			System.out.print("Confronto diretto del predicato \""+inputTriple[1].toLowerCase()+"\" senza l'uso di sinonimi:");
	  	  		}
	  	  		
	  	  		if(stepSearch==1){	// confronto dei sinonimi del predicato della tripla d'ingresso con i predicati del db
	  	  		synonymsDirectBestPos = getSynonyms(inputTriple[1].toLowerCase(), false, false);
	  	  			if(synonymsDirectBestPos == null) 
	  	  				numberSynonymsFound = 0;
	  	  			else
	  	  				numberSynonymsFound = synonymsDirectBestPos.length;
	  	  			for(i = 0; i < numberSynonymsFound; i++)
	  	  				predicates[i] = new Term(count_line_db, synonymsDirectBestPos[i].toLowerCase());
	  	  			System.out.print("Confronto con "+numberSynonymsFound+" sinonimi più probabili di \""+inputTriple[1].toLowerCase()+"\":");
	  	  		}
	  	  		
	  	  		if(stepSearch==2){	// confronto dei sinonimi di tutti i significati del predicato della tripla d'ingresso con i sinonimi dei predicati del db
	  	  		synonymsDirectEveryPos = getSynonyms(inputTriple[1].toLowerCase(), false, true);
	  	  			if(synonymsDirectEveryPos == null) 
	  	  				numberSynonymsFound = 0;
	  	  			else
	  	  				numberSynonymsFound = synonymsDirectEveryPos.length;
	  	  			for(i = 0; i < numberSynonymsFound; i++)
	  	  				predicates[i] = new Term(count_line_db, synonymsDirectEveryPos[i].toLowerCase());
	  	  			System.out.print("Confronto tra sinonimi probabili dei predicati del database con "+numberSynonymsFound+" sinonimi probabili di \""+inputTriple[1].toLowerCase()+"\":");
	  	  		}
	  	  		
	  	  		if(stepSearch==3){	// confronto dei sinonimi pesanti del predicato della tripla d'ingresso con i sinonimi di tutti i significati dei predicati del db
	  	  		synonymsIndirectBestPos = getSynonyms(inputTriple[1].toLowerCase(), true, false);
	  	  			if(synonymsIndirectBestPos == null) 
	  	  				numberSynonymsFound = 0;
	  	  			else
	  	  				numberSynonymsFound = synonymsIndirectBestPos.length;
	  	  			for(i = 0; i < numberSynonymsFound; i++)
	  	  				predicates[i] = new Term(count_line_db, synonymsIndirectBestPos[i].toLowerCase());
	  	  			System.out.print("Confronto tra i sinonimi probabili di tutti i significati dei predicati del database con "+numberSynonymsFound+" sinonimi di \""+inputTriple[1].toLowerCase()+"\":");
	  	  		}
	  	  		
	  	  		if(stepSearch==4 || stepSearch==5){	// confronto dei sinonimi pesanti di tutti i significati del predicato della tripla d'ingresso con i sinonimi pesanti di (prima migliore significato, poi tutti i significati) dei predicati del db
	  	  		synonymsIndirectEveryPos = getSynonyms(inputTriple[1].toLowerCase(), true, true);
	  	  			if(synonymsIndirectEveryPos == null) 
	  	  				numberSynonymsFound = 0;
	  	  			else
	  	  				numberSynonymsFound = synonymsIndirectEveryPos.length;
	  	  			for(i = 0; i < numberSynonymsFound; i++)
	  	  				predicates[i] = new Term(count_line_db, synonymsIndirectEveryPos[i].toLowerCase());
	  	  			if(stepSearch == 4) System.out.print("Confronto massiccio tra i sinonimi dei predicati del database con "+numberSynonymsFound+" sinonimi di tutti i significati di \""+inputTriple[1].toLowerCase()+"\":");
	  	  			if(stepSearch == 5) System.out.print("Confronto massiccio tra i sinonimi di tutti i significati dei predicati del database con "+numberSynonymsFound+" sinonimi di tutti i significati di \""+inputTriple[1].toLowerCase()+"\":");
	  	  		}
	  	  		
	  	  		System.out.println();
	  	  		for(i = 0; i<numberSynonymsFound; i++ ){
	  	  			predicates[i].searchWordsMatchInDb(db);
	  	  		tripleFound = compareTerm(subject, predicates[i], complement, db, tripleFound, stepSearch);
	  	  		}
	  	  		stepSearch++;
	  	  		if(!tripleFound)
	  	  			System.out.println("		Nessuna corrispondenza trovata.");
	  	  		if((stepSearch == 3 || stepSearch == 5 || tripleFound) && stepSearch != 6){
	  	  			if(stepSearch != 3 && stepSearch != 5) System.out.print("	Proseguire con la ricerca? [s/n] ");
	  	  			if(stepSearch == 3) System.out.print("	Si vuole intensificare la ricerca? [s/n] ");
	  	  			if(stepSearch == 5) System.out.print("	Si vuole intensificare ulteriormente la ricerca? (sono possibili risultati inattesi) [s/n] ");
	  	  			do{
	  	  				try { 
	  	  					str = myInput.readLine(); 
	  	  				} 
	  	  				catch (IOException e) { 
	  	  					System.out.println ("Si è verificato un errore: " + e);
	  	  					System.exit(-1); 
	  	  				}
	  	  				if(str.equals("n"))
	  	  					tripleFound = true;
	  	  				if(str.equals("s"))
	  	  					tripleFound = false;
	  	  			}
	  	  			while(!str.equals("s") && !str.equals("n"));
	  	  		}
			}	// del while
	  	  System.out.println("Si vuole effettuare una ulteriore ricerca? [s/n] ");
	  	try { 
				str = myInput.readLine(); 
			} 
			catch (IOException e) { 
				System.out.println ("Si è verificato un errore: " + e);
				System.exit(-1); 
			}
			if(str.equals("s"))
				loop = true;
		}	// del do pluriricerca
		while(loop);
	  	System.out.println();
	  	System.out.println ("Esecuzione terminata.");
	}	// del main

}





