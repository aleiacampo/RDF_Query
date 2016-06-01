
public class Term {

	String word;
	
	int subjectsFoundInDb[];
	int predicateFoundInDb[];
	int complementsFoundInDb[];
	int predicateSynonymsFoundInDbDirectBestPos[];
	int predicateSynonymsFoundInDbDirectEveryPos[];
	int predicateSynonymsFoundInDbIndirectBestPos[];
	int predicateSynonymsFoundInDbIndirectEveryPos[];
	
	int countSubject;
	int countPredicate;
	int countComplement;	
	int countDirectBestPos;
	int countDirectEveryPos;
	int countIndirectBestPos;
	int countInirectEveryPos;

	boolean findSubject;
	boolean findPredicate;
	boolean findComplement;
	boolean findDirectBestPos;
	boolean findDirectEveryPos;
	boolean findIndirectBestPos;
	boolean findIndirectEveryPos;
	
	
	Term(int DIM_DB, String term){
		
		word = term;
		subjectsFoundInDb = new int[DIM_DB];
		predicateFoundInDb = new int[DIM_DB];
		complementsFoundInDb = new int[DIM_DB];
		predicateSynonymsFoundInDbDirectBestPos = new int[DIM_DB];
		predicateSynonymsFoundInDbDirectEveryPos = new int[DIM_DB];
		predicateSynonymsFoundInDbIndirectBestPos = new int[DIM_DB];
		predicateSynonymsFoundInDbIndirectEveryPos = new int[DIM_DB];
		
		int j;
		for(j=0; j<DIM_DB; j++){
			subjectsFoundInDb[j] = -1;
			predicateFoundInDb[j] = -1;
			complementsFoundInDb[j] = -1;
			predicateSynonymsFoundInDbDirectBestPos[j] = -1;
			predicateSynonymsFoundInDbDirectEveryPos[j] = -1;
			predicateSynonymsFoundInDbIndirectBestPos[j] = -1;
			predicateSynonymsFoundInDbIndirectEveryPos[j] = -1;
		}
		
		countSubject = 0;
		countPredicate = 0;
		countComplement = 0;	
		countDirectBestPos = 0;
		countDirectEveryPos = 0;
		countIndirectBestPos = 0;
		countInirectEveryPos = 0;

		findSubject = false;
		findPredicate = false;
		findComplement = false;
		findDirectBestPos = false;
		findDirectEveryPos = false;
		findIndirectBestPos = false;
		findIndirectEveryPos = false;
		
	}
	
	public void searchWordsMatchInDb(Database db){
		
		int i, j;
		  for(i = 0; i < db.rows; i++){
			  if(word.equals(db.values[i][0])){	// se il soggetto è uguale salvo la tripla corrispondente
				subjectsFoundInDb[countSubject++]=i;
				findSubject = true;
			  }
			  if(word.equals(db.values[i][1])){	// se il pred. è uguale salvo la tripla corrispondente
				  predicateFoundInDb[countPredicate++]=i;
				  findPredicate = true;
			  }
			  if(word.equals(db.values[i][2])){	// se il comp. è uguale salvo la tripla corrispondente
				  complementsFoundInDb[countPredicate++]=i;
				  findComplement = true;
			  }
			  if(db.predicatesDirectBestPos[i] != null)	// se il vettore contenente i sinonimi diretti best_pos del predicato della tripla #i presente nel db 
				  for(j = 0; j < db.predicatesDirectBestPos[i].length; j++) // scorro tutti i diretti_best_pos per vedere se trovo il predicato che cerco (this.parola)
					  if(word.equals(db.predicatesDirectBestPos[i][j])){	// se lo trovo, salvo la sua posizione
						  predicateSynonymsFoundInDbDirectBestPos[countDirectBestPos++]=i;
						  findDirectBestPos = true;
					  }
			  if(db.predicatesDirectEveryPos[i] != null)	// se il vettore contenente i sinonimi diretti every_pos del predicato della tripla #i presente nel db 
				  for(j = 0; j < db.predicatesDirectEveryPos[i].length; j++) // scorro tutti i diretti_every_pos per vedere se trovo il predicato che cerco (this.parola)
					  if(word.equals(db.predicatesDirectEveryPos[i][j])){	// se lo trovo, salvo la sua posizione
						  predicateSynonymsFoundInDbDirectEveryPos[countDirectEveryPos++]=i;
						  findDirectEveryPos = true;
					  }
			  if(db.predicatesInirectBestPos[i] != null)	
				  for(j = 0; j < db.predicatesInirectBestPos[i].length; j++) 
					  if(word.equals(db.predicatesInirectBestPos[i][j])){	
						  predicateSynonymsFoundInDbIndirectBestPos[countIndirectBestPos++]=i;
						  findIndirectBestPos = true;
					  }
			  if(db.predicatesInirectEveryPos[i] != null)
				  for(j = 0; j < db.predicatesInirectEveryPos[i].length; j++) 
					  if(word.equals(db.predicatesInirectEveryPos[i][j])){
						  predicateSynonymsFoundInDbIndirectEveryPos[countInirectEveryPos++]=i;
						  findIndirectEveryPos = true;
					  }
		  }
	}

}
