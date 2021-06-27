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
import java.util.Objects;
import java.lang.Math;

public class AlphaBeta{
	//static QuartoBoard originalBoard, workingBoard;
	
	int agentPlayerNumber = 0;
	static Node submittedAction = new Node();
	List <Node> pieceList;
	static int first=0;
	static int last1=0;
	static double depthLimit = 2;
	//static int depth;
	public static int lastmove[]= new int[2];
	public static int maxMove[]= new int[2];
	Node found;
	static long maxScore= Long.MIN_VALUE;
	boolean critical;
	List <Node> availableChoices = new ArrayList <Node> ();
	List <Node> availableChoicesforFirstTime = new ArrayList <Node> ();
	
	
	public long eval(QuartoBoard workingBoard){
		int rowValue= calculateRow(lastmove[0], workingBoard);
		int ProbabilityOfRow= rowValue/5;
		int columnValue = calculateColumn(lastmove[1], workingBoard);
		int ProbabilityOfColumn= columnValue/5;
		int firstDiagonalValue=0;
		int ProbabilityOfFirstDiagonal=0;
		int secondDiagonalValue=0;
		int ProbabilityOfSecondDiagonal=0;
		
		if(lastmove[0]==lastmove[1]){
			firstDiagonalValue= calculateFirstDiagonals(workingBoard);
			ProbabilityOfFirstDiagonal=firstDiagonalValue/5;
		}
		if(Math.abs(lastmove[0]+lastmove[1])==4){
			secondDiagonalValue= calculateSecondDiagonals(workingBoard);
			ProbabilityOfSecondDiagonal=secondDiagonalValue/5;
		}
		
		
		// if(Math.abs(lastmove[0]-lastmove[1])==0)
		// {
			// firstDiagonalValue= calculateFirstDiagonals(workingBoard);
			// ProbabilityOfFirstDiagonal=firstDiagonalValue/5;
		// }
		long total=rowValue+ columnValue+ secondDiagonalValue+ firstDiagonalValue;
		//System.out.println(Long.toString(total)+"\n");
		return (total);
		
	}
	int x=0;
	
	public long AlphaBetaPrunning(QuartoBoard CopyBoard, int pieceID, int depth, long alpha, long beta)
	{	
		//List <Node> availableChoices = new ArrayList <Node> ();
		QuartoBoard originalBoard = new QuartoBoard(CopyBoard);
		QuartoBoard workingBoard=new QuartoBoard(originalBoard);
		//QuartoBoard piece1= new QuartoPiece(workingBoard);
		
		
		if(depth==0 || workingBoard.checkIfBoardIsFull())
		{
			//System.out.println("Print\n");
			return eval(workingBoard);
			
		}
		
		//long score= Integer.MIN_VALUE;
		generateMissingActions(availableChoices ,pieceID, workingBoard);
		
		
		//x+= sizeOfChoices;
		
		//sizeOfChoices+=availableChoices.size();
		MirrorSimilarities(0, availableChoices.size(), workingBoard, availableChoices);
		long score = Long.MIN_VALUE;
		RotationSimilarities(0,availableChoices.size(),workingBoard,availableChoices);
		last1=availableChoices.size();
		//System.out.println("FIRST="+Integer.toString(first)+"LAST= "+Integer.toString(last1));
		//System.out.println("---------------"+Integer.toString(x)+"     "+Integer.toString(depth)+"   "+Integer.toString(availableChoices.size()));
		//System.out.println(pieceID);
		/* for(int i=first;i< last1;i++){
			QuartoBoard workingBoard1=new QuartoBoard(originalBoard);
			workingBoard.insertPieceOnBoard(availableChoices.get(i).move[0],availableChoices.get(i).move[1],availableChoices.get(i).pieceID);
			workingBoard.printBoardState();
			//System.out.println(Integer.toString(i)+"-----"+ Integer.toString(depth)+ "-------------"+ Integer.toString(availableChoices.get(i).pieceID));
		} */
		for(int i=0;i< last1;i++){
	//		System.out.println(i);
			
			workingBoard.insertPieceOnBoard(availableChoices.get(i).move[0],availableChoices.get(i).move[1],availableChoices.get(i).pieceID);
			if(depth==1){
			lastmove[0]=availableChoices.get(i).move[0];
			lastmove[1]=availableChoices.get(i).move[1];}
			//System.out.println(Integer.toString(availableChoices.get(i).move[0])+"   "+ Integer.toString(availableChoices.get(i).move[1])+"   "+ availableChoices.get(i).pieceID+"    "+ Integer.toString(depth));
			//System.out.println(Integer.toString(depth)+"        "+Integer.toString(availableChoices.size())+"\n");
			//workingBoard.printBoardState();
			/* if(depth==1){
			lastmove[0]=availableChoices.get(i).move[0];
			lastmove[1]=availableChoices.get(i).move[1];
			System.out.println("CHOSEN MOVE"+ Integer.toString(lastmove[0])+ Integer.toString(lastmove[1]));
			} */
			//workingBoard.printBoardState();
			//System.out.println(Integer.toString(i)+"-----"+ Integer.toString(depth)+ "-------------"+ Integer.toString(availableChoices.get(i).pieceID));
			//System.out.println(availableChoices.get(i));
			long cur= (-1) * AlphaBetaPrunning(workingBoard,workingBoard.chooseNextPieceNotPlayed(), depth-1, -beta, -alpha);
			if(cur> score){
				score= cur;
				//stem.out.println(Integer.toString(maxMove[0])+"  "+ Integer.toString(maxMove[1])+"  "+ Long.toString(cur)+"\n");
			}
			if(score> alpha){
				alpha=score;
			}
			
			if(alpha>=beta){
				break;
			}
			// System.out.println(availableChoices.get(i).move[0]);
			// System.out.println(" , ");
			// System.out.println(availableChoices.get(i).move[1]);
		}
		first=last1;
		return alpha;
	}
	
	
	public int[] mov(QuartoBoard copyBoard, int pieceID){
		AlphaBeta search= new AlphaBeta(); 
		//check as semi random agent
		boolean skip = false;
         int row,col=0;
         for (row = 0; row < copyBoard.getNumberOfRows(); row++) {
             for (col = 0; col < copyBoard.getNumberOfColumns(); col++) {
                 if (!copyBoard.isSpaceTaken(row, col)) {
                     QuartoBoard copyBoard1 = new QuartoBoard(copyBoard);
                     copyBoard1.insertPieceOnBoard(row, col, pieceID);
                     if (copyBoard1.checkRow(row) || copyBoard1.checkColumn(col) || copyBoard1.checkDiagonals()) {
                         skip = true;
                         break;
                     }
                 }
             }
             if (skip) {
                 break;
             }
         }
		 if(skip){
			int[] move= new int[2];
			move[0]=row;
			move[1]=col;
			return move;}
			
		generateMissingActions(availableChoicesforFirstTime ,pieceID, copyBoard);
		MirrorSimilarities(0, availableChoicesforFirstTime.size(), copyBoard, availableChoicesforFirstTime);
		RotationSimilarities(0,availableChoicesforFirstTime.size(),copyBoard,availableChoicesforFirstTime);
		long MaximumScore=Long.MIN_VALUE;
		for(int i=0;i<availableChoicesforFirstTime.size();i++){
			QuartoBoard workingBoard2=new QuartoBoard(copyBoard);
			workingBoard2.insertPieceOnBoard(availableChoicesforFirstTime.get(i).move[0],availableChoicesforFirstTime.get(i).move[1],availableChoicesforFirstTime.get(i).pieceID);
			long currentScore= AlphaBetaPrunning(workingBoard2, workingBoard2.chooseNextPieceNotPlayed(),3, Long.MIN_VALUE, Long.MAX_VALUE);
			if(currentScore>=MaximumScore)
			{
				MaximumScore=currentScore;
				maxMove[0]=availableChoicesforFirstTime.get(i).move[0];
				maxMove[1]=availableChoicesforFirstTime.get(i).move[1];
			}
		}
		// originalBoard = new QuartoBoard(copyBoard);
		// workingBoard = new QuartoBoard(originalBoard);
		//generateMissingActions(pieceID);
		// for(int i=0;i< availableChoices.size();i++)
		// {
			// System.out.println(availableChoices.get(i).move[0]);
		// }
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
					
					// if (pieceID == -1) {
						
						// for (int piece = 0; piece < originalBoard.getNumberOfPieces(); piece++) {
							// if (!originalBoard.isPieceOnBoard(piece)) {
								
								// int nextPiece = originalBoard.chooseNextPieceNotPlayed(piece);
																
					    		// Create a new Node for each individual choice at the current turn
							    // Node individualChoice = new Node();
							    
							    // individualChoice.player = agentPlayerNumber;
							    
							    // individualChoice.pieceID = nextPiece;
							    
					    		// Add next possible choice to the new Node
								// individualChoice.move[0] = row;
								// individualChoice.move[1] = column;
				
								// Add Node containing a possible choice to the availableChoices array
								// availableChoices.add(individualChoice);
	
							// }
						// }	
					// }
					// else if (pieceID != -1){
						
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
		
	
	
	
	
	
	
	
	
	
	
	
//Because tree is no longer static, this is not needed
//	public void pruneTree (List <Node> children) {
//		
//		if (!children.isEmpty()){
//			//agentPlayerNumber = children.get(0).player;
//			
//			for (Node e : children) {
//				
//				e.parent = null;
//				
//			}
//			
//			availableChoices = children;
//		}
//	}

	
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
	
	
	

public void MirrorSimilarities(int start, int last, QuartoBoard workingBoard, List <Node> availableChoices){
	
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
				//last1--;
				}
	
			}
	
	
		}
	//System.out.println("---------------------Symetries---------");
	}
	
	
	public void RotationSimilarities(int start, int last, QuartoBoard workingBoard, List <Node> availableChoices){
		//QuartoBoard RotatedBoard= new QuartoBoard();
		for(int i=0; i<availableChoices.size();i++){
			QuartoBoard Board1= new QuartoBoard(workingBoard);
			Board1.insertPieceOnBoard(availableChoices.get(i).move[0], availableChoices.get(i).move[1], availableChoices.get(i).pieceID);
			int[][] arr= new int[Board1.getNumberOfRows()][Board1.getNumberOfColumns()];
			for(int j=0;j< Board1.getNumberOfRows(); j++){
				for(int k=0; k<Board1.getNumberOfColumns();k++)
				{
					QuartoPiece piece1=Board1.getPieceOnPosition(j,k);
					try
					{
						arr[j][k]= piece1.getPieceID();
					}
					catch(NullPointerException e){
						
						arr[j][k]=-1;
					} 
				}
			}
			for(int x=0;x< 4;x++){
				///////////
				for(int j=0; j< Board1.getNumberOfRows();j++){
					for(int k=0; k< j;k++)
					{
						int temp=arr[j][k];
						arr[j][k]= arr[k][j];
						arr[k][j]= temp;
					}
				}
				for(int j=0; j< Board1.getNumberOfRows();j++)
				{
					for(int k=0; k< (Board1.getNumberOfColumns())/2; k++)
					{
						int temp=arr[j][k];
						arr[j][k]= arr[j][(Board1.getNumberOfColumns())-k-1];
						arr[j][(Board1.getNumberOfColumns())-k-1]= temp;
					}
				}
				
				for(int f=i; f< availableChoices.size();f++){
					QuartoBoard Board2 = new QuartoBoard(workingBoard);
					Board2.insertPieceOnBoard(availableChoices.get(f).move[0], availableChoices.get(f).move[1], availableChoices.get(f).pieceID);
					boolean notEqual=false;
					for(int j=0;j< Board1.getNumberOfRows();j++){
						for(int k=0;k< Board1.getNumberOfColumns();k++){
							
							QuartoPiece piece2=Board2.getPieceOnPosition(j,k);
							int check;
							try{
									check=piece2.getPieceID();
							}
							catch(NullPointerException e){
								check=-1;
							
							}
							if(arr[j][k]!=check)
							{
								notEqual=true;
								break;
							}
						
						}
						if(notEqual==true) break;
						
					}
					if(notEqual==false){
						availableChoices.remove(availableChoices.get(f));
						//last1--;
					}
				}
				
			}
			
		}
	}
}