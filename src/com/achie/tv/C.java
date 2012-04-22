package com.achie.tv;

public class C {
	
	public static final class app {
	}
	
	public static interface syncResponseCode {
		public static final int IO_EXCEPTION 				= 901;
		public static final int CLIENT_PROTOCOL_EXCEPTION 	= 902;

		public static final int SERVER_UNAVAILABLE 			= -10;
		
		public static final int SUCCESS 					= 0;
		public static final int FAIL_FROM_SERVER 			= 1;
		public static final int FAIL_UNKNOWN_REASON 		= -1;
	}
	
	public static interface extras {
		public static final String ABOUT 				= "about";
	}
	
	public static interface jsonKeys {
		public static final String IN_APP_ID 			= "inAppId";
		
		public static final String SHOW_NAME 			= "showName";
		public static final String START_TIME			= "startTime";
		public static final String END_TIME 			= "endTime";
		public static final String SEASON_INFO 			= "seasonInformation";
		public static final String EPISODE_INFO 		= "episodeInformation";
		public static final String IMAGE_URL 			= "imageUrl";
		public static final String DESCRIPTION 			= "description";
		public static final String CHANNEL 				= "channel";
		public static final String CONTESTANTS 			= "contestants";
		public static final String SHOW_ID 				= "showId";
		
		public static final String CONT_NAME 			= "name";
		public static final String CONT_ID 				= "contestantId";
		public static final String CONT_INFO 			= "contestantInfo";
		public static final String CONT_IMG_URL 		= "imgUrl";
		public static final String CONT_VOTES 			= "voteCount";
		public static final String CONT_RANK 			= "rank";
	}
	
	public static interface constants {
	}
}