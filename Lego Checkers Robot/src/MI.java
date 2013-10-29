import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class MI 
{
	RemoteNXTFunctions NXT;
	private Stack<Move> simulatedMoves = new Stack<Move>();
	MI()
	{
		try {
			NXT = new RemoteNXTFunctions();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/* */
	private int numberofmovelook = 3;
	public void LookForMove()
	{
	    List<Move> Moves = possibleMovesForRobot();
		Move BestMove;
		
		double Price, tempPrice;
		
		for(Move move : Moves)
		{
			tempPrice =  OpponentTurn(move, 1);
			
			if(Price < tempPrice)
			{
				Price = tempPrice;
				BestMove = move;
			}
		}
		/* do move  */
	}
	private double OwnTurn(Move move, int moveLook)
	{
		int numberOfMoves = 0;
		double sum = 0;
		double price = 0;
		
		if(numberofmovelook >= moveLook)
		{
			price = FindOwnPrice(move);
			/* do move on representation board */
			List<Move> Moves = possibleMovesForRobot();
			for(Move tempMove : Moves)
			{
				sum += OwnTurn(tempMove, moveLook);
				numberOfMoves++;
			}
			/* undo move on representation board */
			sum = sum/numberOfMoves;
			moveLook ++;
		}
		return price + sum;
	}
	
	
	
	
	
	
	
	private List<Move> possibleMovesForHuman()
	{
		return possibleMoves(-1);
	}
	
	private List<Move> possibleMovesForRobot()
	{
		return possibleMoves(1);
	}
	
	private void simulateMove(Field fromField, Field toField) throws Exception
	{
		Move temp = new Move(fromField, toField, false);
		if(!fromField.isEmpty())
		{
			NXT.checkersBoard.movePiece(fromField, toField);
			simulatedMoves.push(temp);
		}
	}
	
	private void revertMove() throws Exception
	{
		if(simulatedMoves.size() != 0)
		{
			Move temp = simulatedMoves.pop();
			NXT.checkersBoard.movePiece(temp.moveTo, temp.moveFrom);
		}
	}
	
	private List<Move> possibleMoves(int moveForSide) //-1 = human, 1 = robot
	{
		List<Move> movements = new ArrayList<Move>();
		
		for(Field[] f : NXT.checkersBoard.myBoard)
		{
			for(Field field : f)
			{
				if(field.getPieceOnField().isMoveable)
				{
					List<Field> possibleMoves = NXT.checkersBoard.checkMoveable(field, moveForSide);
					//Simple moves
					if(!possibleMoves.isEmpty())
					{
						for(Field posField : possibleMoves)
						{
							Move movement = new Move(field, posField, false);
							movements.add(movement);
						}
					}
					
					//Jumps
					Field jumpDirectionForward = NXT.checkersBoard.checkJumpDirection(field, 1, moveForSide, false);
					if(jumpDirectionForward != null)
					{
						Move movement = new Move(field, jumpDirectionForward, true);
						movements.add(movement);
					}
					
					Field jumpDirectionBackwards = NXT.checkersBoard.checkJumpDirection(field, -1, moveForSide, false);
					if(jumpDirectionBackwards != null)
					{
						Move movement = new Move(field, jumpDirectionBackwards, true);
						movements.add(movement);
					}
				}
			}
		}
		
		return movements;
	}
}
