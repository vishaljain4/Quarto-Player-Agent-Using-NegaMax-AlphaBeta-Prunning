public class QuartoPlayerAgent extends QuartoAgent {
		
    public QuartoPlayerAgent(GameClient gameClient, String stateFileName) {
        // because super calls one of the super class constructors(you can overload constructors), you need to pass the parameters required.
        super(gameClient, stateFileName);
    }

    //MAIN METHOD
    public static void main(String[] args) {
        //start the server
        GameClient gameClient = new GameClient();

        String ip = null;
        String stateFileName = null;
        //IP must be specified
        if(args.length > 0) {
            ip = args[0];
        } else {
            System.out.println("No IP Specified");
            System.exit(0);
        }
        if (args.length > 1) {
            stateFileName = args[1];
        }

        gameClient.connectToServer(ip, 4321);
        QuartoPlayerAgent quartoAgent = new QuartoPlayerAgent(gameClient, stateFileName);
		
        quartoAgent.play();

        gameClient.closeConnection();
    
    }

    @Override
    protected String pieceSelectionAlgorithm() {
    
    	this.startTimer();
	           
        QuartoBoard copyBoard = new QuartoBoard(this.quartoBoard);
                
        MonteCarloSearch search = new MonteCarloSearch();
                
        int pieceID = search.getMCS(copyBoard);

        String binaryString = String.format("%5s", Integer.toBinaryString(pieceID)).replace(' ', '0');
        
        return binaryString;
 
    }

    @Override
    protected String moveSelectionAlgorithm(int pieceID) {
        
        QuartoBoard copyBoard = new QuartoBoard(this.quartoBoard);
        AlphaBeta search = new AlphaBeta();
        int[] move = new int[2];
		// MonteCarloSearch search = new MonteCarloSearch();
		
        move= search.mov(copyBoard, pieceID);
		// move= search.getMCS(copyBoard, pieceID);
		//System.out.println("SCORE--"+ Integer.toString(score));
        return move[0] + "," + move[1];
        
    }

}
