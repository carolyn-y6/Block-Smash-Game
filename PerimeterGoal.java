package assignment3;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] block = board.flatten();
		int score = 0;
		for(int i = 0; i < block.length; i++){
			if(block[i][0] == targetGoal){
				score++;
			}
			if(block[i][block.length-1] == targetGoal){
				score++;
			}
		}

		for(int j = 0; j < block.length; j++){
			if(block[0][j] == targetGoal){
				score++;
			}
			if(block[block.length-1][j] == targetGoal){
				score++;
			}
		}
		return score;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
