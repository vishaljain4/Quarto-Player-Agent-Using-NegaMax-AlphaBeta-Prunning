public class available_moves{
	int[][] moves= int [25][25];
	int size;
	
	public void availableChoices(QuartoBoard workingBoard, int pieceID)
	{
		int i=0;
		for (int row = 0; row < workingBoard.getNumberOfRows(); row++) {
			
			for (int column = 0; column < workingBoard.getNumberOfColumns(); column++) {
				
				if (!workingBoard.isSpaceTaken(row, column)) {
					moves[i][0]=row;
					moves[i][1]=column;
					i++;
					
				}
			}
		}
		size=i;
	}
	
}