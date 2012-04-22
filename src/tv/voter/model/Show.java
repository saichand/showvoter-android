package tv.voter.model;

import java.util.List;

public class Show {
    private String showName;

    private long startTime;

    private long endTime;

    private String seasonInformation;

    private String episodeInformation;

    private String imageUrl;

    private String description;

    private Channel channel;

    private List<Contestant> contestants;
    
    private List<Vote> votes;
    
    
    private long showId;
    
    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getSeasonInformation() {
        return seasonInformation;
    }

    public void setSeasonInformation(String seasonInformation) {
        this.seasonInformation = seasonInformation;
    }

    public String getEpisodeInformation() {
        return episodeInformation;
    }

    public void setEpisodeInformation(String episodeInformation) {
        this.episodeInformation = episodeInformation;
    }

    /**
     * @return the imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl
     *            the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * @param channel
     *            the channel to set
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /*public HashMap<Contestant, Vote> getContestantVoteMap() {
        return contestantVoteMap;
    }

    public void setContestantVoteMap(HashMap<Contestant, Vote> contestantVoteMap) {
        this.contestantVoteMap = contestantVoteMap;
    }*/
    
    
    
    public long getShowId() {
        return showId;
    }

    public void setShowId(long showId) {
        this.showId = showId;
    }
    
    public List<Contestant> getContestants() {
        return contestants;
    }

    public void setContestants(List<Contestant> contestants) {
        this.contestants = contestants;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Show Name: ").append(showName);
        buffer.append("\n");
        buffer.append("Season: ").append(seasonInformation);
        buffer.append("\n");
        buffer.append("Episode: ").append(episodeInformation);
        buffer.append("\n");
        if (channel != null) {
            buffer.append("Channel Number: ").append(channel.getChannelNumber());
            buffer.append("\n");
            buffer.append("Channel Name: ").append(channel.getChannelName());
            buffer.append("\n");
        }
        buffer.append("Start Time: ").append(startTime);
        buffer.append("\n");
        buffer.append("End Time: ").append(endTime);
        buffer.append("\n");
        buffer.append("Image URL: ").append(imageUrl);
        buffer.append("\n");
        buffer.append("\n");
        buffer.append("Contestants");
        buffer.append("------------");
        buffer.append("\n");
        if (contestants != null && contestants.size() > 0) {
            for (Contestant cont : contestants) {
                buffer.append("Name: ").append(cont.getName());
                buffer.append("\n");
                buffer.append("ID: ").append(cont.getContestantId());
                buffer.append("\n");
                buffer.append("Info: ").append(cont.getContestantInfo());
                buffer.append("\n");
                buffer.append("Img URL: ").append(cont.getImgUrl());
                buffer.append("\n");
                buffer.append("\n");
            }
        }
        
        return buffer.toString();
    }

}
