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


public class AlphaBeta1{
List <Node> availableChoices = new ArrayList <Node> ();
static int last_visited_choice=0;
	public int[] mov(QuartoBoard copyBoard, int pieceID)
	{
			AlphaBeta1 search= new AlphaBeta1(); 
			available_moves availableMoves= new available_moves();
			availableMoves.availableChoices(copyBoard,pieceID);
			for(int i=0;i<availableMoves.size;i++){
				copyBoard.insertPieceOnBoard(choices.moves[i][0], choices.moves[i][1],pieceID);
				int evaluationResult= -AlphaBetaPrunning(copyBoard, copyBoard.chooseNextPieceNotPlayed,5, Long.MIN_VALUE, Long.MAX_VALUE);
				
			}
			
			
			int[] move= new int[2];
			move[0]=maxMove[0];
			move[1]=maxMove[1];
			return move;
	}
	

	public int AlphaBetaPrunning(QuartoBoard copyBoard, int pieceID, int depth, long alpha, long beta)
	{	
		int[][] available_moves=new int [5][5];
		QuartoBoard originalBoard = new QuartoBoard(CopyBoard);
		QuartoBoard workingBoard = new QuartoBoard(originalBoard);
		if(depth==0 || workingBoard.checkIfBoardIsFull())
		{
			return eval(workingBoard);
		}
		
		long score= Long.MIN_VALUE;
		available_moves choices = new available_moves();
		choices.availableChoices(workingBoard,pieceID);
		for(int i=0; i< choices.size;i++)
		{
			workingBoard.insertPieceOnBoard(choices.moves[i][0], choices.moves[i][1],pieceID);
			long value= -AlphaBetaPrunning(workingBoard,workingBoard.chooseNextPieceNotPlayed(),depth-1,-beta,-alpha);
			
		}
		
		
	}
	
	










}