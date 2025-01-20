namespace CEngineCopy
{
    internal class Board
    {
        public static ulong[] bitboard_array_global = new ulong[12];
        public static int ep_global;
        public static bool[] castle_rights_global = new bool[4];
        public static bool is_white_global;

        public static void SetStartingPosition()
        {

            ep_global = Constants.NO_SQUARE;
            is_white_global = true;
            castle_rights_global[0] = true;
            castle_rights_global[1] = true;
            castle_rights_global[2] = true;
            castle_rights_global[3] = true;

            bitboard_array_global[Constants.WP] = Constants.WP_STARTING_POSITIONS;
            bitboard_array_global[Constants.WN] = Constants.WN_STARTING_POSITIONS;
            bitboard_array_global[Constants.WB] = Constants.WB_STARTING_POSITIONS;
            bitboard_array_global[Constants.WR] = Constants.WR_STARTING_POSITIONS;
            bitboard_array_global[Constants.WQ] = Constants.WQ_STARTING_POSITION;
            bitboard_array_global[Constants.WK] = Constants.WK_STARTING_POSITION;
            bitboard_array_global[Constants.BP] = Constants.BP_STARTING_POSITIONS;
            bitboard_array_global[Constants.BN] = Constants.BN_STARTING_POSITIONS;
            bitboard_array_global[Constants.BB] = Constants.BB_STARTING_POSITIONS;
            bitboard_array_global[Constants.BR] = Constants.BR_STARTING_POSITIONS;
            bitboard_array_global[Constants.BQ] = Constants.BQ_STARTING_POSITION;
            bitboard_array_global[Constants.BK] = Constants.BK_STARTING_POSITION;

        }

    }
}
