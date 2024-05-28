package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] unitCells = board.flatten();
		boolean[][] visited = new boolean[unitCells.length][unitCells[0].length];
		int score = 0;
		for(int i = 0; i <unitCells.length; i++){
			for(int j = 0; j <unitCells[0].length; j++){
				int tempscore = undiscoveredBlobSize(i, j, unitCells, visited);
				if(score < tempscore){
					score = tempscore;
				}
			}
		}
		return score;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		if(targetGoal != unitCells[i][j]){
			return 0;
		}
		if(!visited[i][j]) {
			int size = 1;
			visited[i][j] = true;
			//for one block up
			if(j-1 >= 0) {
				size += undiscoveredBlobSize(i, j - 1, unitCells, visited);
			}
			//for one block down
			if(j+1 < unitCells[0].length) {
				size += undiscoveredBlobSize(i, j + 1, unitCells, visited);
			}
			//for one block left
			if(i-1 >= 0) {
				size += undiscoveredBlobSize(i - 1, j, unitCells, visited);
			}
			//for one block right
			if(i+1 < unitCells.length) {
				size += undiscoveredBlobSize(i + 1, j, unitCells, visited);
			}
			return size;
		}
		return 0;
	}
}
