package com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.client.result.EmailAddressParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;

import java.util.ArrayList;
import java.util.List;

public class EmailResultAdapter extends RecyclerView.Adapter<EmailResultAdapter.EmailViewHolder> {

    public static class EmailResultItem {
        public final EmailResultItemType type;
        public final String content;

        EmailResultItem(EmailResultItemType type, String content) {
            this.type = type;
            this.content = content;
        }
    }

    public enum EmailResultItemType {
        TYPE_TO(R.string.item_result_email_to),
        TYPE_CC(R.string.item_result_email_cc),
        TYPE_BCC(R.string.item_result_email_bcc),
        TYPE_SUBJECT(R.string.item_result_email_subject),
        TYPE_BODY(R.string.item_result_email_body);

        @StringRes int local;

        EmailResultItemType(int local) {
            this.local = local;
        }
    }

    private List<EmailResultItem> resultItems = new ArrayList<>();

    private EmailResultAdapter(@NonNull List<EmailResultItem> resultItems) {
        this.resultItems.addAll(resultItems);
    }

    public EmailResultAdapter(EmailAddressParsedResult result) {
        this(buildResultItems(result));
    }

    private static List<EmailResultItem> buildResultItems(EmailAddressParsedResult result) {
        List<EmailResultItem> items = new ArrayList<>();

        if(result != null) {
            if(result.getTos() != null) {
                for(String to : result.getTos()) {
                    items.add(new EmailResultItem(EmailResultItemType.TYPE_TO, to));
                }
            }
            if(result.getCCs() != null) {
                for(String cc : result.getCCs()) {
                    items.add(new EmailResultItem(EmailResultItemType.TYPE_CC, cc));
                }
            }
            if(result.getBCCs() != null) {
                for(String bcc : result.getBCCs()) {
                    items.add(new EmailResultItem(EmailResultItemType.TYPE_BCC, bcc));
                }
            }
            if(result.getSubject() != null) {
                items.add(new EmailResultItem(EmailResultItemType.TYPE_SUBJECT, result.getSubject()));
            }
            if(result.getBody() != null) {
                items.add(new EmailResultItem(EmailResultItemType.TYPE_BODY, result.getBody()));
            }
        }
        return items;
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.item_result_email, viewGroup, false);
//        switch(EmailResultItemType.values()[viewType]) {
//            case TYPE_CC:
//                v = inflater.inflate(R.layout.item_result_email, viewGroup, false);
//                break;
//            case TYPE_BCC:
//                v = inflater.inflate(R.layout.item_result_email, viewGroup, false);
//                break;
//            case TYPE_SUBJECT:
//                v = inflater.inflate(R.layout.item_result_email, viewGroup, false);
//                break;
//            case TYPE_BODY:
//                v = inflater.inflate(R.layout.item_result_email, viewGroup, false);
//                break;
//            case TYPE_TO: default:
//                v = inflater.inflate(R.layout.item_result_email, viewGroup, false);
//                break;
//
//        }
        return new EmailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder viewHolder, int i) {
        Context context = viewHolder.content.getContext();
        viewHolder.content.setText(resultItems.get(i).content);
        viewHolder.type.setText(context.getString(resultItems.get(i).type.local));
    }

    @Override
    public int getItemCount() {
        return resultItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return resultItems.get(position).type.ordinal();
    }

    class EmailViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView type;
        EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.item_result_email_content);
            type = itemView.findViewById(R.id.item_result_email_type);
        }
    }
}
