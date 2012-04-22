package tv.voter.model;

import java.util.List;

public class Contestant {
    private String name;

    private long contestantId;

    private String contestantInfo;
    
    private String imgUrl;

    private List<String> videoUrls;

    private String contestantCode;
    
    private long voteCount;
    
    private String rank;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getContestantId() {
        return contestantId;
    }

    public void setContestantId(long contestantId) {
        this.contestantId = contestantId;
    }

    public String getContestantInfo() {
        return contestantInfo;
    }

    public void setContestantInfo(String contestantInfo) {
        this.contestantInfo = contestantInfo;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<String> getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(List<String> videoUrls) {
        this.videoUrls = videoUrls;
    }

    public String getContestantCode() {
        return contestantCode;
    }

    public void setContestantCode(String contestantCode) {
        this.contestantCode = contestantCode;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
    
}
