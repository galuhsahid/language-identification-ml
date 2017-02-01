import java.util.*;
import java.io.*;
public class FeatureGenerator{
	public static void main(String[] args) throws Exception
	{
		jalanin();
		//bikinGram(2,2);
	}

	public static void jalanin() throws Exception
	{
		boolean loop=false;
		int bool = 0;
		while(true){

			System.out.println("Pilih fitur boolean atau frekuensi: [Ketik q untuk keluar]");
			System.out.println("1. Boolean");
			System.out.println("2. Frekuensi");
			Scanner scan = new Scanner(System.in);
			String pilihan = scan.nextLine();
			if(pilihan.equals("1"))
			{
				System.out.println("Anda memilih fitur boolean");
				loop = true;
				bool = 1;
				break;
			}
			else if(pilihan.equals("2"))
			{
				System.out.println("Anda memilih fitur boolean");
				loop = true;
				bool = 2;
				break;
			}
			else if(pilihan.equals("q"))
			{
				System.out.println("Keluar");
				break;
			}
			else
			{
				System.out.println("Anda hanya bisa memasukkan input 1 atau 2");
			}
		}
		while(loop)
		{
			System.out.println("Pilih nilai N-gram yang diinginkan: [Ketik q untuk keluar]");
			System.out.println("1. 1-gram");
			System.out.println("2. 2-gram");
			System.out.println("3. 3-gram");
			System.out.println("4. 4-gram");
			
			System.out.println();
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();	
			if(input.equals("q"))
			{
				System.out.println("Keluar");
				break;
			}
			if(input.equals("1") ||input.equals("2") ||input.equals("3") ||input.equals("4"))
			{
				System.out.println("Anda memilih " + input + "-gram");
				loop = false;
				bikinGram(Integer.parseInt(input), bool);
				break;
			}
			else
			{
				System.out.println("Anda hanya bisa memasukkan input 1, 2, 3, atau 4");
			}
		}
	}
	public static void bikinGram(int n, int fitur) throws Exception
	{		
		BufferedReader reader  = new BufferedReader(new FileReader("Sentences.txt"));		
		String line = "";
		ArrayList<Sentence> sList = new ArrayList<Sentence>();
		while ((line = reader.readLine()) != null) 
		{
			String[] splitter = line.split(";");
			String kelas = splitter[splitter.length-1]; 
			String kalimat = "";
			for(int i = 1; i < splitter.length-1; i++)
			{
				kalimat += splitter[i] + " ";
			}
			Sentence mySentence = new Sentence (splitter[0], kalimat.toLowerCase(), kelas);
			sList.add(mySentence);
		}
		//BIKIN 1 GRAM
		if(n==1)
		{
			HashMap<String, Integer> myHash = new HashMap<String, Integer>();
			//bikin 1-gram
			int total = 0;
			for(int i = 0; i < sList.size();i++)
			{
				Sentence current = sList.get(i);
				String[] words = current.isi.split(" ");
				for(int j = 0; j < words.length; j++)
				{
					if(!words[j].equals("") )
					{
						if(myHash.get(words[j]) == null)
						{
							myHash.put(words[j],1);
							total++;
						}
						else
						{
							int count = myHash.get(words[j]);
							myHash.put(words[j], count+1);
							total++;
						}
					}
				}
			}

			ArrayList<MyPair> pList = new ArrayList<MyPair>();
			for (String key: myHash.keySet()) 
			{
				int val = myHash.get(key);
			    MyPair p = new MyPair(key, val);

			    if(val > 1)
			    	pList.add(p);
			}

			Collections.sort(pList);
			int counter = 1;
			ArrayList<String> featureList = new ArrayList<String>();
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Feature.txt")));
			System.out.println("Processing...");
			writer.append("nomor_kalimat,");
			ArrayList<String> features = new ArrayList<String>();
			for(MyPair mp : pList)
			{
				if(counter>100 && mp.kata.length() > 1)
				{
					//System.out.println(mp.kata + " " + mp.number);
					writer.append(mp.kata+",");
					features.add(mp.kata);
				}
				counter++;
			}

			writer.append("bahasa\n");
			for(int i = 0; i < sList.size();i++)
			{
				Sentence current = sList.get(i);
				String[] words = current.isi.split(" ");
				String hasil = "" + (int)(i+1) + ",";
				for(int j = 0; j < features.size(); j++){
					int featureCounter = 0;
					for(int k = 0; k < words.length; k++){
						if(features.get(j).equals(words[k]))
							featureCounter++;
					}
					if(fitur == 1){
						if(featureCounter > 0)
							hasil += "1,";
						else
							hasil += "0,";
					}
					else if (fitur==2){
						hasil += "" + featureCounter + ",";
					}
				}
				writer.append(hasil + sList.get(i).label+ "\n");
				//System.out.println(hasil);
			}
			writer.flush();
			writer.close();
		}
		else if(n == 2) //Bikin 2-Gram
		{	
			HashMap<String, Integer> myHash = new HashMap<String, Integer>();			
			int total = 0;		
			List<String> ngramList = new ArrayList<String>();		
			
  			//GENERATE THE N-GRAMS
  			for(int i = 0; i < sList.size();i++)
			{	
				Sentence current = sList.get(i);
				String[] words = current.isi.split(" ");
				for(int k=0; k<(words.length-n+1); k++){
    					String s="";
    					int start=k;
   					int end=k+n;
    					for(int j=start; j<end; j++){
     						s=s+" "+words[j];
   					}
    					//Add n-gram to a list
    					ngramList.add(s);

  				}

			}
			//for(int i = 0; i < ngramList.size(); i++) {
            		//	System.out.println(ngramList.get(i));
        		//}
			for(int k = 0; k < ngramList.size(); k++) {
				String ngram = ngramList.get(k);
				if(!ngram.equals("") )
				{
					if(myHash.get(ngram) == null)
					{
						myHash.put(ngram,1);
						total++;
					}
					else
					{
						int count = myHash.get(ngram);
						myHash.put(ngram, count+1);
						total++;
					}
				}
			}
			
			ArrayList<MyPair> pList = new ArrayList<MyPair>();
			for (String key: myHash.keySet()) 
			{
				int val = myHash.get(key);
			    MyPair p = new MyPair(key, val);

			   // if(val > 1)
			    	pList.add(p);
			}

			Collections.sort(pList);
			int counter = 1;
			ArrayList<String> featureList = new ArrayList<String>();
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Feature.txt")));
			System.out.println("Processing...");
			writer.append("no,");
			ArrayList<String> features = new ArrayList<String>();
			for(MyPair mp : pList)
			{
				//if(counter>100 && mp.kata.length() > 1)
				//{
					//System.out.println(mp.kata + " " + mp.number);
				if(counter <= 100 && mp.kata.length() > 1)
				{
					writer.append(mp.kata+",");
					features.add(mp.kata);
				}
				//}
				counter++;
			}

			writer.append("bahasa\n");
			for(int i = 0; i < sList.size();i++)
			{
				
				List<String> nGram = new ArrayList<String>();		
				Sentence current = sList.get(i);
				String[] words = current.isi.split(" ");
				for(int k=0; k<(words.length-n+1); k++){
    					String s="";
    					int start=k;
   					int end=k+n;
    					for(int j=start; j<end; j++){
     						s=s+" "+words[j];
   					}
					nGram.add(s);
  				}				
				String hasil = "" + (int)(i+1) + ",";
				for(int j = 0; j < features.size(); j++){
					int featureCounter = 0;
					for(int k = 0; k < nGram.size(); k++){
						if(features.get(j).equals(nGram.get(k)))
							featureCounter++;
					}
					if(fitur == 1){
						if(featureCounter > 0)
							hasil += "1,";
						else
							hasil += "0,";
					}
					else if (fitur==2){
						hasil += "" + featureCounter + ",";
					}
				}
				writer.append(hasil + sList.get(i).label+ "\n");
				//System.out.println(hasil);
			}
			writer.flush();
			writer.close();
		}

		else if(n == 3) //Bikin 3-Gram
		{	
			HashMap<String, Integer> myHash = new HashMap<String, Integer>();			
			int total = 0;		
			List<String> ngramList = new ArrayList<String>();		
			
  			//GENERATE THE N-GRAMS
  			for(int i = 0; i < sList.size();i++)
			{	
				Sentence current = sList.get(i);
				String[] words = current.isi.split(" ");
				for(int k=0; k<(words.length-n+1); k++){
    					String s="";
    					int start=k;
   					int end=k+n;
    					for(int j=start; j<end; j++){
     						s=s+" "+words[j];
   					}
    					//Add n-gram to a list
    					ngramList.add(s);

  				}

			}
			//for(int i = 0; i < ngramList.size(); i++) {
            		//	System.out.println(ngramList.get(i));
        		//}
			for(int k = 0; k < ngramList.size(); k++) {
				String ngram = ngramList.get(k);
				if(!ngram.equals("") )
				{
					if(myHash.get(ngram) == null)
					{
						myHash.put(ngram,1);
						total++;
					}
					else
					{
						int count = myHash.get(ngram);
						myHash.put(ngram, count+1);
						total++;
					}
				}
			}
			
			ArrayList<MyPair> pList = new ArrayList<MyPair>();
			for (String key: myHash.keySet()) 
			{
				int val = myHash.get(key);
			    MyPair p = new MyPair(key, val);

			   // if(val > 1)
			    	pList.add(p);
			}

			Collections.sort(pList);
			int counter = 1;
			ArrayList<String> featureList = new ArrayList<String>();
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Feature.txt")));
			System.out.println("Processing...");
			writer.append("no,");
			ArrayList<String> features = new ArrayList<String>();
			for(MyPair mp : pList)
			{
				//if(counter>100 && mp.kata.length() > 1)
				//{
					//System.out.println(mp.kata + " " + mp.number);
				if(counter <= 100 && mp.kata.length() > 1)
				{
					writer.append(mp.kata+",");
					features.add(mp.kata);
				}
				//}
				counter++;
			}

			writer.append("bahasa\n");
			for(int i = 0; i < sList.size();i++)
			{
				
				List<String> nGram = new ArrayList<String>();		
				Sentence current = sList.get(i);
				String[] words = current.isi.split(" ");
				for(int k=0; k<(words.length-n+1); k++){
    					String s="";
    					int start=k;
   					int end=k+n;
    					for(int j=start; j<end; j++){
     						s=s+" "+words[j];
   					}
					nGram.add(s);
  				}				
				String hasil = "" + (int)(i+1) + ",";
				for(int j = 0; j < features.size(); j++){
					int featureCounter = 0;
					for(int k = 0; k < nGram.size(); k++){
						if(features.get(j).equals(nGram.get(k)))
							featureCounter++;
					}
					if(fitur == 1){
						if(featureCounter > 0)
							hasil += "1,";
						else
							hasil += "0,";
					}
					else if (fitur==2){
						hasil += "" + featureCounter + ",";
					}
				}
				writer.append(hasil + sList.get(i).label+ "\n");
				//System.out.println(hasil);
			}
			writer.flush();
			writer.close();
		}

		else if(n == 4) //Bikin 4-Gram
		{	
			HashMap<String, Integer> myHash = new HashMap<String, Integer>();			
			int total = 0;		
			List<String> ngramList = new ArrayList<String>();		
			
  			//GENERATE THE N-GRAMS
  			for(int i = 0; i < sList.size();i++)
			{	
				Sentence current = sList.get(i);
				String[] words = current.isi.split(" ");
				for(int k=0; k<(words.length-n+1); k++){
    					String s="";
    					int start=k;
   					int end=k+n;
    					for(int j=start; j<end; j++){
     						s=s+" "+words[j];
   					}
    					//Add n-gram to a list
    					ngramList.add(s);

  				}

			}
			//for(int i = 0; i < ngramList.size(); i++) {
            		//	System.out.println(ngramList.get(i));
        		//}
			for(int k = 0; k < ngramList.size(); k++) {
				String ngram = ngramList.get(k);
				if(!ngram.equals("") )
				{
					if(myHash.get(ngram) == null)
					{
						myHash.put(ngram,1);
						total++;
					}
					else
					{
						int count = myHash.get(ngram);
						myHash.put(ngram, count+1);
						total++;
					}
				}
			}
			
			ArrayList<MyPair> pList = new ArrayList<MyPair>();
			for (String key: myHash.keySet()) 
			{
				int val = myHash.get(key);
			    MyPair p = new MyPair(key, val);

			    //if(val > 1)
			    	pList.add(p);
			}

			Collections.sort(pList);
			int counter = 1;
			ArrayList<String> featureList = new ArrayList<String>();
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Feature.txt")));
			System.out.println("Processing...");
			writer.append("no,");
			ArrayList<String> features = new ArrayList<String>();
			for(MyPair mp : pList)
			{
				//if(counter>100 && mp.kata.length() > 1)
				//{
					//System.out.println(mp.kata + " " + mp.number);
				if(counter <= 100 && mp.kata.length() > 1)
				{
					writer.append(mp.kata+",");
					features.add(mp.kata);
				}
				//}
				counter++;
			}

			writer.append("bahasa\n");
			for(int i = 0; i < sList.size();i++)
			{
				
				List<String> nGram = new ArrayList<String>();		
				Sentence current = sList.get(i);
				String[] words = current.isi.split(" ");
				for(int k=0; k<(words.length-n+1); k++){
    					String s="";
    					int start=k;
   					int end=k+n;
    					for(int j=start; j<end; j++){
     						s=s+" "+words[j];
   					}
					nGram.add(s);
  				}				
				String hasil = "" + (int)(i+1) + ",";
				for(int j = 0; j < features.size(); j++){
					int featureCounter = 0;
					for(int k = 0; k < nGram.size(); k++){
						if(features.get(j).equals(nGram.get(k)))
							featureCounter++;
					}
					if(fitur == 1){
						if(featureCounter > 0)
							hasil += "1,";
						else
							hasil += "0,";
					}
					else if (fitur==2){
						hasil += "" + featureCounter + ",";
					}
				}
				writer.append(hasil + sList.get(i).label+ "\n");
				//System.out.println(hasil);
			}
			writer.flush();
			writer.close();
		}
	}
}

class Sentence
{
	String id;
	String isi;
	String label;

	public Sentence(String myId, String myIsi, String myLabel)
	{
		this.id = myId;
		this.isi = myIsi;
		this.isi = myIsi.replaceAll("[.,?!;'\"!@#$%^&*()-=_+\\[\\]{}:<>/|\\\\]","").replaceAll(" {2,}"," ");
		this.label =myLabel;
	}
}

class MyPair implements Comparable<MyPair>
{
	String kata;
	int number;

	public MyPair(String kkata, int nnumber)
	{
		this.kata = kkata;
		this.number = nnumber;
	}

	public int compareTo(MyPair other)
	{
		return other.number - this.number;
	}
}
