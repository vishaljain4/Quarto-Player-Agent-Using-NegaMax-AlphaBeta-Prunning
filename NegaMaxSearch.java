import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class NegaMaxSearch{
	//static QuartoBoard originalBoard, workingBoard;
	
	int agentPlayerNumber = 0;
	static Node submittedAction = new Node();
	List <Node> pieceList;
	static double depthLimit = 2;
	//static int depth;
	public int lastmove[]= new int[2];
	public int maxMove[]= new int[2];
	Node found;
	boolean critical;
	
	public long eval(QuartoBoard workingBoard){
		int rowValue= calculateRow(lastmove[0], workingBoard);
		int ProbabilityOfRow= rowValue/5;
		int columnValue = calculateColumn(lastmove[1], workingBoard);
		int ProbabilityOfColumn= columnValue/5;
		int firstDiagonalValue=0;
		int ProbabilityOfFirstDiagonal=0;
		int secondDiagonalValue=0;
		int ProbabilityOfSecondDiagonal=0;
		List <Node> availableChoices = new ArrayList <Node> ();
		if(lastmove[0]==lastmove[1]){
			firstDiagonalValue= calculateFirstDiagonals(workingBoard);
			ProbabilityOfFirstDiagonal=firstDiagonalValue/5;
		}
		if(lastmove[0]+lastmove[1]==4){
			secondDiagonalValue= calculateSecondDiagonals(workingBoard);
			ProbabilityOfSecondDiagonal=secondDiagonalValue/5;
		}
		long total=rowValue+ columnValue+ secondDiagonalValue+ firstDiagonalValue;
		//System.out.println(Long.toString(total)+"\n");
		return (total);
		
	}
	int x=0;
	
	public long NegaMaxSearch(QuartoBoard CopyBoard, int pieceID, int depth)
	{	
		List <Node> availableChoices = new ArrayList <Node> ();
		QuartoBoard originalBoard = new QuartoBoard(CopyBoard);
		QuartoBoard workingBoard=new QuartoBoard(originalBoard);
		//QuartoBoard piece1= new QuartoPiece(workingBoard);
		
		if(depth==0 || workingBoard.checkIfBoardIsFull())
		{
			//System.out.println("Print\n");
			return eval(workingBoard);
			
		}
		
		long score= Integer.MIN_VALUE;
		generateMissingActions(availableChoices ,pieceID, workingBoard);
		
		//x+= sizeOfChoices;
		
		//sizeOfChoices+=availableChoices.size();
		Similarities(0, availableChoices.size(), workingBoard, availableChoices);
		//System.out.println("---------------"+Integer.toString(x)+"     "+Integer.toString(depth)+"   "+Integer.toString(availableChoices.size()));
		//System.out.println(pieceID);
		for(int i=0;i< availableChoices.size();i++){
	//		System.out.println(i);
			workingBoard.insertPieceOnBoard(availableChoices.get(i).move[0],availableChoices.get(i).move[1],availableChoices.get(i).pieceID);
			
			//System.out.println(Integer.toString(availableChoices.get(i).move[0])+"   "+ Integer.toString(availableChoices.get(i).move[1])+"   "+ availableChoices.get(i).pieceID+"    "+ Integer.toString(depth));
			//System.out.println(Integer.toString(depth)+"        "+Integer.toString(availableChoices.size())+"\n");
			//workingBoard.printBoardState();
			if(depth==1){
			lastmove[0]=availableChoices.get(i).move[0];
			lastmove[1]=availableChoices.get(i).move[1];
			}
			//System.out.println(availableChoices.get(i));
			long cur= (-1) * NegaMaxSearch(workingBoard,workingBoard.chooseNextPieceNotPlayed(), depth-1);
			if(cur> score){
				maxMove[0]=lastmove[0];
				maxMove[1]=lastmove[1];
				score= cur;
				//System.out.println(Integer.toString(maxMove[0])+"  "+ Integer.toString(maxMove[1])+"  "+ Long.toString(cur)+"\n");
			}
			
		}
		return score;
	}
	
	public int[] mov(QuartoBoard copyBoard, int pieceID){
		NegaMaxSearch search= new NegaMaxSearch(); 
		NegaMaxSearch(copyBoard, pieceID,6);
		int[] move= new int[2];
		move[0]=maxMove[0];
		move[1]=maxMove[1];
		return move;
	}
	
	public void setPieceOnBoard(List <Node> availableChoices ,QuartoBoard workingBoard, int[] availableChoicesmove, int pieceID){
			workingBoard.getPiece(pieceID).setPosition(availableChoicesmove[0], availableChoicesmove[1]);
			workingBoard.board[availableChoicesmove[0]][availableChoicesmove[1]] = workingBoard.getPiece(pieceID);
	
	}
	
	public void generateMissingActions (List <Node> availableChoices,int pieceID, QuartoBoard originalBoard) {
		
		for (int row = 0; row < originalBoard.getNumberOfRows(); row++) {
			
			for (int column = 0; column < originalBoard.getNumberOfColumns(); column++) {
				
				if (!originalBoard.isSpaceTaken(row, column)) {
					
					
						
			    		//Create a new Node for each individual choice at the current turn
					    Node individualChoice = new Node();
					    
					    //individualChoice.player = agentPlayerNumber;
					    
					    individualChoice.pieceID = pieceID;
						
			    		//Add next possible choice to the new Node
						individualChoice.move[0] = row;
						individualChoice.move[1] = column;
		
						//Add Node containing a possible choice to the availableChoices array
						availableChoices.add(individualChoice);
						
					}
				}
			}
		}		
	
	
	
	


	
	//Code from QuartoSemiRandomAgent and modified
	//loop through board and see if the game is in a won state
    private boolean checkIfGameIsWon(QuartoBoard copyBoard) {

        //loop through rows
        for(int i = 0; i < copyBoard.getNumberOfRows(); i++) {
            //gameIsWon = this.quartoBoard.checkRow(i);
            if (copyBoard.checkRow(i)) {
                return true;
            }

        }
        //loop through columns
        for(int i = 0; i < copyBoard.getNumberOfColumns(); i++) {
            //gameIsWon = this.quartoBoard.checkColumn(i);
            if (copyBoard.checkColumn(i)) {
                return true;
            }

        }

        //check Diagonals
        if (copyBoard.checkDiagonals()) {
            return true;
        }

        return false;
    }
	
	
	public int calculateColumn(int column, QuartoBoard workingBoard) {

		boolean[] characteristics;
		int[] commonCharacteristics = new int[] {0, 0, 0, 0, 0};

		for(int row = 0; row < workingBoard.getNumberOfRows(); row++) {
			QuartoPiece piece = workingBoard.getPieceOnPosition(row, column);
			if(piece == null) {
				continue;
			}
			characteristics = piece.getCharacteristicsArray();
			for(int i = 0; i < commonCharacteristics.length; i++) {
				commonCharacteristics[i] = commonCharacteristics[i] + (characteristics[i] ? 1 : 0);
			}
		}

		//loop through the commonCharacteristics array
		//if the value is either 0 or 5 for any commonCharacteristics[i], all 5 pieces share that characteristic
		int maxColumn=0;
		for(int i = 0; i < commonCharacteristics.length; i++) {
			if (commonCharacteristics[i] >maxColumn){
			maxColumn=commonCharacteristics[i];
			}
		}
		return maxColumn;
	}
	
	
	
	
	public int calculateRow(int row, QuartoBoard workingBoard) {

		boolean[] characteristics;
		int[] commonCharacteristics = new int[] {0, 0, 0, 0, 0};

		for(int column = 0; column < workingBoard.getNumberOfColumns(); column++) {
			QuartoPiece piece = workingBoard.getPieceOnPosition(row, column);
			if(piece == null) {
				continue;
			}
			characteristics = piece.getCharacteristicsArray();
			for(int i = 0; i < commonCharacteristics.length; i++) {
				commonCharacteristics[i] = commonCharacteristics[i] + (characteristics[i] ? 1 : 0);
			}
		}

		int maxRow=0;
		//loop through the commonCharacteristics array
		//if the value is either 0 or 5 for any commonCharacteristics[i], all 5 pieces share that characteristic
		for(int i = 0; i < commonCharacteristics.length; i++) {
			if (commonCharacteristics[i] >maxRow){
			maxRow=commonCharacteristics[i];
			}
		}

		return maxRow;

	}
	
	
	
	
	
	
	
	
		//checks the Diagonals
	public int calculateFirstDiagonals(QuartoBoard workingBoard) {
		boolean[] characteristics;
		int[] commonCharacteristics = new int[] {0, 0, 0, 0, 0};
		boolean unableToWinFirstDiagonal = false;


		for(int row = 0, column = 0; row < workingBoard.getNumberOfRows(); row++, column++) {
			QuartoPiece piece = workingBoard.getPieceOnPosition(row, column);
			if(piece == null) {
				continue;
			}
			characteristics = piece.getCharacteristicsArray();
			for(int i = 0; i < commonCharacteristics.length; i++) {
				commonCharacteristics[i] = commonCharacteristics[i] + (characteristics[i] ? 1 : 0);
			}
		}
			
			int maxFirstDiagonal=0;
		
			for(int i = 0; i < commonCharacteristics.length; i++) {
				if (commonCharacteristics[i] >maxFirstDiagonal) {
					maxFirstDiagonal=commonCharacteristics[i];
				}
			}
			return maxFirstDiagonal;
		
		}
		
	public int calculateSecondDiagonals(QuartoBoard workingBoard){

		boolean[] characteristics;
		int[] commonCharacteristics = new int[] {0, 0, 0, 0, 0};

		for(int row = workingBoard.board.length - 1, column = 0; row >= 0; row--, column++) {
			QuartoPiece piece = workingBoard.getPieceOnPosition(row, column);
			if(piece == null) {
				continue;
			}
			characteristics = piece.getCharacteristicsArray();
			for(int i = 0; i < commonCharacteristics.length; i++) {
				commonCharacteristics[i] = commonCharacteristics[i] + (characteristics[i] ? 1 : 0);
			}

		}
		int maxSecondDiagonal=0;
		for(int i = 0; i < commonCharacteristics.length; i++) {
			if (commonCharacteristics[i] >maxSecondDiagonal) {
				maxSecondDiagonal=commonCharacteristics[i];
			}
		}

		return maxSecondDiagonal;
	}
	
	
	

public void Similarities(int start, int last, QuartoBoard workingBoard, List <Node> availableChoices){
	
	boolean notEqual=false;
	for(int i=0; i<availableChoices.size();i++){
		QuartoBoard Board1= new QuartoBoard(workingBoard);
		Board1.insertPieceOnBoard(availableChoices.get(i).move[0], availableChoices.get(i).move[1], availableChoices.get(i).pieceID);
		for(int j=i;j<availableChoices.size();j++){
				QuartoBoard Board2= new QuartoBoard(workingBoard);	
				Board2.insertPieceOnBoard(availableChoices.get(j).move[0], availableChoices.get(j).move[1], availableChoices.get(j).pieceID);
				
				for(int k=0; k<(workingBoard.getNumberOfRows())/2;k++){
					for(int l=0;l<workingBoard.getNumberOfColumns();l++){
						QuartoPiece piece1 = Board1.getPieceOnPosition(k, l);
						QuartoPiece piece2 = Board2.getPieceOnPosition(workingBoard.getNumberOfRows()-1-k, l);
						
						if(piece1!=piece2){
							notEqual=true;
							break;
						}
						
					}
					if(notEqual==true){
						//System.out.println("NOT EQUAL1");
							break;
						}
					}
				
				if(notEqual==true){
				for(int k=0; k<(workingBoard.getNumberOfColumns())/2;k++){
					for(int l=0;l<workingBoard.getNumberOfRows();l++){
						QuartoPiece piece1 = Board1.getPieceOnPosition(l, k);
						QuartoPiece piece2 = Board2.getPieceOnPosition(l,workingBoard.getNumberOfColumns()-1-k);
						if(piece1!=piece2){
							notEqual=true;
							break;
						}
						
					}
					if(notEqual==true){
							//System.out.println("NOT EQUAL2");
							break;
						}
				
				
				
					}
				}
			if(notEqual==false){
				availableChoices.remove(availableChoices.get(j));	
				}
	
			}
	
	
		}
	//System.out.println("---------------------Symetries---------");
	}
	
	
	
}