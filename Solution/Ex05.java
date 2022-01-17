import org.pg4200.les11.BitReader;
import org.pg4200.les11.BitWriter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Ex05{

    /*
    * (SOURCES)
    * GradeCompressorImp.java
    * DnaCompressor.java
    * */

    //number: 0-n (chess moves) //the estimated average of chess moves in a game is 41.4 moves (according to https://www.chessgames.com/chessstats.html ) . I will delegate 6 bits in this task.
    //pieces: 6 (3-bits)
    //p=pawn, r=rook, k=knight, b=bishop, q=queen, K=king
    //A-H:    8 (4-bits)
    //1-8     8 (4-bits)
    //check   1 (1-bit)

    //length is num + 5 + !

    public byte[] compress(String chessMoves) {

        BitWriter writer = new BitWriter();
        String regex = "\\d+[bprkqK][a-h][0-8][a-h][0-8]\\!?";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(chessMoves);

        while (m.find()){
            for(int i=0; i <= m.groupCount(); i++){

                String currentMove = m.group(i);

                boolean isCheck = ('!' == currentMove.charAt(m.group(i).length()-1));
                int startMoveNum = Integer.parseInt(currentMove.substring(0, (isCheck ? currentMove.length()-6 : currentMove.length()-5)));
                int startMoveNumLength = String.valueOf(startMoveNum).length();
                char piece = pieceExchange(currentMove.charAt(startMoveNumLength), true);
                char originLetter = currentMove.charAt(startMoveNumLength+1);
                int originNum = currentMove.charAt(startMoveNumLength+2);
                char destLetter = currentMove.charAt(startMoveNumLength+3);
                int destNum = currentMove.charAt(startMoveNumLength+4);

                writer.write(startMoveNum, 6);
                writer.write(piece - 'A', 3);
                writer.write(originLetter - 'a', 4);
                writer.write(originNum, 4);
                writer.write(destLetter - 'a', 4);
                writer.write(destNum, 4);
                writer.write(isCheck);

                System.out.println("startMove:"+ startMoveNum +  " originL:" +  originLetter + " originN:" + originNum + " destL:" + destLetter + " destN:" + destNum + " isCheck:" + isCheck);
            }
        }

        return writer.extract();
    }

    private char pieceExchange(char piece, boolean fromPiece){
        //p=pawn, r=rook, k=knight, b=bishop, q=queen, K=king
        //A       B       C         D         E        F

        if(fromPiece){
            switch (piece){
                case 'p': return 'A';
                case 'r': return 'B';
                case 'k': return 'C';
                case 'b': return 'D';
                case 'q': return 'E';
                case 'K': return 'F';
            }
        }

        if(!fromPiece){
            switch (piece){
                case 'A': return 'p';
                case 'B': return 'r';
                case 'C': return 'k';
                case 'D': return 'b';
                case 'E': return 'q';
                case 'F': return 'K';
            }
        }
        return 'A';
    }

    public String decompress(byte[] data){

        BitReader reader = new BitReader(data);
        String result = "";
        int entries = (data.length*8) / 26;

        for(int i=0; i<entries; i++){
            result += reader.readInt(6);
            result += pieceExchange((char)('A' + reader.readInt(3)), false);
            result += (char)('a' + reader.readInt(4));
            result += reader.readInt(4);
            result += (char)('a' + reader.readInt(4));
            result += reader.readInt(4);

            if(reader.readBoolean()){
                result += '!';
            } else {
                result += "";
            }
        }
        return result;
    }
}
