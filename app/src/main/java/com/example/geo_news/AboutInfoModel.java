package com.example.geo_news;

public class AboutInfoModel {
    int memberImage, memberGitHubLink;
    String memberName, memberRole;

    public AboutInfoModel(int memberImage, String memberName, int memberGitHubLink, String memberRole) {
        this.memberImage = memberImage;
        this.memberName = memberName;
        this.memberGitHubLink = memberGitHubLink;
        this.memberRole = memberRole;
    }

    public int getMemberImage() {
        return memberImage;
    }

    public void setMemberImage(int memberImage) {
        this.memberImage = memberImage;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getMemberGitHubLink() {
        return memberGitHubLink;
    }

    public void setMemberGitHubLink(int memberGitHubLink) {
        this.memberGitHubLink = memberGitHubLink;
    }

    public String getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(String memberRole) {
        this.memberRole = memberRole;
    }
}
