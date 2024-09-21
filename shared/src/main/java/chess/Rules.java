//package chess;
//
//public class Rules {
//    static private final HashMap<PieceType, MovementRule> rules = new HashMap<>();
//
//    static {
//        rules.put(KING, new KingMovementRule());
//        rules.put(QUEEN, new QueenMovementRule());
//        rules.put(KNIGHT, new KnightMovementRule());
//        rules.put(BISHOP, new BishopMovementRule());
//        rules.put(ROOK, new RookMovementRule());
//        rules.put(PAWN, new PawnMovementRule());
//
//    }
//    static public MovementRule pieceRule(PieceType type) {
//        return rules.get(type);
//    }
//
//}
