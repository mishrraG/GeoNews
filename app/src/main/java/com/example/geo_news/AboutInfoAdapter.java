package com.example.geo_news;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AboutInfoAdapter extends RecyclerView.Adapter<AboutInfoAdapter.ViewHolder> {

    private List<AboutInfoModel> memberList;

    public AboutInfoAdapter(List<AboutInfoModel> memberList) {
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aboutinfo_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int image = memberList.get(position).getMemberImage();
        String name = memberList.get(position).getMemberName();
        String role = memberList.get(position).getMemberRole();
        int gitLink = memberList.get(position).getMemberGitHubLink();

        holder.setMemberImage(image);
        holder.setMemberName(name);
        holder.setMemberRole(role);
        holder.setMemberGithubLink(gitLink);

    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView memberImage;
        private TextView memberName, memberRole, memberGithubLink;
        public ViewHolder(View itemview) {
            super(itemview);
            memberImage = itemview.findViewById(R.id.memberImageView);
            memberName = itemview.findViewById(R.id.memberNameTextView);
            memberRole = itemview.findViewById(R.id.memberRoleTextView);
            memberGithubLink = itemview.findViewById(R.id.memberGithubLinkTextView);

            memberGithubLink.setMovementMethod(LinkMovementMethod.getInstance());
        }
        private void setMemberImage(int image){
            memberImage.setImageResource(image);
        }

        private void setMemberName(String name){
            memberName.setText(name);
        }

        private void setMemberRole(String role){
            memberRole.setText(role);
        }

        private void setMemberGithubLink(int gitLink) {
            memberGithubLink.setText(gitLink);
        }
    }
}
