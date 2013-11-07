import java.io.IOException;
import customExceptions.IllegalMove;
import customExceptions.NoKingLeft;
import lejos.nxt.Button;

public class SW508E13 {

	public static void main(String[] args) throws IOException, NoKingLeft, InterruptedException {
		RemoteNXTFunctions checkTopFunc = new RemoteNXTFunctions();
		MI mi = new MI(checkTopFunc);

		Move bestMove;
		if(checkTopFunc.checkersBoard.myPeasentColor == 'r')
		{
			bestMove = mi.lookForBestMove();
			checkTopFunc.doMove(bestMove);
			checkTopFunc.resetAfterMove();
		}
		while(!Button.ESCAPE.isDown())
		{
			checkTopFunc.waitForRedButton();
			try {
				if(mi.nXTF.checkersBoard.analyzeFunctions.analyzeBoard())
				{
					bestMove = mi.lookForBestMove();

					if(bestMove != null)
						mi.nXTF.doMove(bestMove);

					checkTopFunc.resetAfterMove();

					if(!mi.nXTF.checkersBoard.analyzeFunctions.checkForGameHasEnded(true))
						mi.nXTF.checkersBoard.informer.playYourTurn();
				}

			} catch (IllegalMove e) {
				mi.nXTF.checkersBoard.informer.illeagalMove();
			}
		}
		/*FakeMI fm1 = new FakeMI(checkTopFunc,true);
		FakeMI fm2 = new FakeMI(checkTopFunc,false);
		if(checkTopFunc.checkersBoard.myPeasentColor == 'r')
		{
			fm1.decideMovement();
			checkTopFunc.getColorOnField(4, -2);
		}
		boolean endGame = true;
		while(!Button.ESCAPE.isDown() && endGame)
		{
			LCD.clear();
			LCD.drawString("human", 0, 0);
			LCD.refresh();
			endGame = fm2.decideMovement();
			
			LCD.clear();
			LCD.drawString("robot", 0, 0);
			LCD.refresh();
			endGame = fm1.decideMovement();
		}*/
	}
}
