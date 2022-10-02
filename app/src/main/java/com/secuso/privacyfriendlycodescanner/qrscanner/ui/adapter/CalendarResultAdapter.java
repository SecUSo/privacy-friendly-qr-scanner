package com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.client.result.AddressBookParsedResult;
import com.secuso.privacyfriendlycodescanner.qrscanner.R;
import com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.resultwrapper.ContactResultWrapper;

/**
 * Displays contact data.
 *
 * @author Christopher Beckmann
 */
public class ContactResultAdapter extends RecyclerView.Adapter<ContactResultAdapter.ViewHolder>{

    private ContactResultWrapper data;

    private static final int TYPE_TEXT = 0;
    private static final int TYPE_NAME = 1;
    private static final int TYPE_PHONE_NUMBER = 2;
    private static final int TYPE_EMAIL = 3;
    private static final int TYPE_INSTANT_MESSENGER = 4;
    private static final int TYPE_NOTE = 5;
    private static final int TYPE_ADDRESS = 6;
    private static final int TYPE_ORGANISATION = 7;
    private static final int TYPE_BIRTHDAY = 8;
    private static final int TYPE_TITLE = 9;
    private static final int TYPE_URL = 10;
    private static final int TYPE_GEO = 11;

    public ContactResultAdapter(AddressBookParsedResult result) {
        this.data = new ContactResultWrapper(result);
    }

    public void setResult(AddressBookParsedResult result) {
        this.data = new ContactResultWrapper(result);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v;
        switch(viewType) {
            case TYPE_NAME:
                v = inflater.inflate(R.layout.item_result_contact_name, viewGroup, false);
                return new NameViewHolder(v);
            case TYPE_PHONE_NUMBER:
                v = inflater.inflate(R.layout.item_result_contact_phone, viewGroup, false);
                return new PhoneViewHolder(v);
            case TYPE_EMAIL:
                v = inflater.inflate(R.layout.item_result_contact_email, viewGroup, false);
                return new EMailViewHolder(v);
            case TYPE_BIRTHDAY:
                v = inflater.inflate(R.layout.item_result_contact_birthday, viewGroup, false);
                return new BirthdayViewHolder(v);
            case TYPE_INSTANT_MESSENGER:
                v = inflater.inflate(R.layout.item_result_contact_messenger, viewGroup, false);
                return new MessengerViewHolder(v);
            case TYPE_ADDRESS:
                v = inflater.inflate(R.layout.item_result_contact_address, viewGroup, false);
                return new AddressViewHolder(v);
            case TYPE_ORGANISATION:
                v = inflater.inflate(R.layout.item_result_contact_organization, viewGroup, false);
                return new OrgViewHolder(v);
            case TYPE_TITLE :
                v = inflater.inflate(R.layout.item_result_contact_title, viewGroup, false);
                return new TitleViewHolder(v);
            case TYPE_URL :
                v = inflater.inflate(R.layout.item_result_contact_url, viewGroup, false);
                return new UrlViewHolder(v);
            case TYPE_GEO :
                v = inflater.inflate(R.layout.item_result_contact_geo, viewGroup, false);
                return new GeoViewHolder(v);
            case TYPE_TEXT:
            case TYPE_NOTE:
            default:
                v = inflater.inflate(R.layout.item_result_contact_text, viewGroup, false);
                return new TextViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        switch(viewHolder.getItemViewType()) {
            case TYPE_NAME:
                NameViewHolder nvh = (NameViewHolder) viewHolder;
                nvh.name.setText(data.getName());
                nvh.pronunciation.setText(data.getPronunciation());
                nvh.pronunciation.setVisibility(data.hasPronunciation() ? View.VISIBLE : View.GONE);
                break;
            case TYPE_PHONE_NUMBER:
                PhoneViewHolder pvh = (PhoneViewHolder) viewHolder;
                pvh.number.setText(data.getPhoneNumber(data.getDataIndex(i)));
                pvh.numberType.setVisibility(data.hasPhoneNumberType(data.getDataIndex(i)) ? View.VISIBLE : View.GONE);
                pvh.numberType.setText(data.getPhoneNumberType(data.getDataIndex(i)));
                break;
            case TYPE_EMAIL:
                EMailViewHolder evh = (EMailViewHolder) viewHolder;
                evh.email.setText(data.getEmail(data.getDataIndex(i)));
                evh.emailType.setVisibility(data.hasEmailType(data.getDataIndex(i)) ? View.VISIBLE : View.GONE);
                evh.emailType.setText(data.getEmailType(data.getDataIndex(i)));
                break;
            case TYPE_INSTANT_MESSENGER:
                MessengerViewHolder mvh = (MessengerViewHolder) viewHolder;
                mvh.messenger.setText(data.getResult().getInstantMessenger());
                break;
            case TYPE_NOTE:
                TextViewHolder notevh = (TextViewHolder) viewHolder;
                notevh.content.setText(data.getResult().getNote());
                break;
            case TYPE_ADDRESS:
                AddressViewHolder avh = (AddressViewHolder) viewHolder;
                avh.address.setText(data.getAddress(data.getDataIndex(i)));
                avh.addressType.setVisibility(data.hasAddressType(data.getDataIndex(i)) ? View.VISIBLE : View.GONE);
                avh.addressType.setText(data.getAddressType(data.getDataIndex(i)));
                break;
            case TYPE_ORGANISATION:
                OrgViewHolder ovh = (OrgViewHolder) viewHolder;
                ovh.org.setText(data.getResult().getOrg());
                break;
            case TYPE_BIRTHDAY:
                BirthdayViewHolder bvh = (BirthdayViewHolder) viewHolder;
                bvh.date.setText(data.getResult().getBirthday());
                break;
            case TYPE_TITLE:
                TitleViewHolder tvh = (TitleViewHolder) viewHolder;
                tvh.title.setText(data.getResult().getTitle());
                break;
            case TYPE_URL:
                UrlViewHolder urlVh = (UrlViewHolder) viewHolder;
                urlVh.url.setText(data.getUrl(data.getDataIndex(i)));
                break;
            case TYPE_GEO:
                GeoViewHolder gvh = (GeoViewHolder) viewHolder;
                gvh.geo.setText(data.getGeo(data.getDataIndex(i)));
                break;
            case TYPE_TEXT:
            default:
                TextViewHolder vh = (TextViewHolder) viewHolder;
                vh.content.setText("");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.getDataCount();
    }

    @Override
    public int getItemViewType(int pos) {
        int currentItemCount = 0;

        if(data.hasNames() || data.hasNickNames() || data.hasPronunciation()) currentItemCount ++;
        if(pos < currentItemCount)
            return TYPE_NAME;

        if(data.hasPhoneNumbers()) currentItemCount += data.getPhoneNumberCount();
        if(pos < currentItemCount)
            return TYPE_PHONE_NUMBER;

        currentItemCount += data.getEmailCount();
        if(pos < currentItemCount)
            return TYPE_EMAIL;

        if(data.hasInstantMessenger()) currentItemCount ++;
        if(pos < currentItemCount)
            return TYPE_INSTANT_MESSENGER;

        if(data.hasNote()) currentItemCount ++;
        if(pos < currentItemCount)
            return TYPE_NOTE;

        currentItemCount += data.getAddressCount();
        if(pos < currentItemCount)
            return TYPE_ADDRESS;

        if(data.hasOrganisation()) currentItemCount ++;
        if(pos < currentItemCount)
            return TYPE_ORGANISATION;

        if(data.hasBirthday()) currentItemCount ++;
        if(pos < currentItemCount)
            return TYPE_BIRTHDAY;

        if(data.hasTitle()) currentItemCount ++;
        if(pos < currentItemCount)
            return TYPE_TITLE;

        currentItemCount += data.getUrlCount();
        if(pos < currentItemCount)
            return TYPE_URL;

        currentItemCount += data.getGeoCount();
        if(pos < currentItemCount)
            return TYPE_GEO;

        return TYPE_TEXT;
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(@NonNull View itemView) { super(itemView); }
    }
    class NameViewHolder extends ViewHolder {
        TextView name;
        TextView pronunciation;

        NameViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_result_name_content);
            pronunciation = itemView.findViewById(R.id.item_result_name_pronunciation);
        }
    }
    class TextViewHolder extends ViewHolder {
        TextView content;

        TextViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.item_result_text_content);
        }
    }
    class PhoneViewHolder extends ViewHolder {
        TextView number;
        TextView numberType;

        PhoneViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.item_result_phone_content);
            numberType = itemView.findViewById(R.id.item_result_phone_number_type);
        }
    }
    class AddressViewHolder extends ViewHolder {
        TextView address;
        TextView addressType;

        AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.item_result_address_content);
            addressType = itemView.findViewById(R.id.item_result_address_type);
        }
    }
    class EMailViewHolder extends ViewHolder {
        TextView email;
        TextView emailType;

        EMailViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.item_result_email_content);
            emailType = itemView.findViewById(R.id.item_result_email_type);
        }
    }
    class BirthdayViewHolder extends ViewHolder {
        TextView date;

        BirthdayViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.item_result_birthday_content);
        }
    }
    class MessengerViewHolder extends ViewHolder {
        TextView messenger;

        MessengerViewHolder(@NonNull View itemView) {
            super(itemView);
            messenger = itemView.findViewById(R.id.item_result_messenger_content);
        }
    }
    class OrgViewHolder extends ViewHolder {
        TextView org;

        OrgViewHolder(@NonNull View itemView) {
            super(itemView);
            org = itemView.findViewById(R.id.item_result_org_content);
        }
    }
    class TitleViewHolder extends ViewHolder {
        TextView title;

        TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_result_title_content);
        }
    }
    class GeoViewHolder extends ViewHolder {
        TextView geo;

        GeoViewHolder(@NonNull View itemView) {
            super(itemView);
            geo = itemView.findViewById(R.id.item_result_geo_content);
        }
    }
    class UrlViewHolder extends ViewHolder {
        TextView url;

        UrlViewHolder(@NonNull View itemView) {
            super(itemView);
            url = itemView.findViewById(R.id.item_result_url_content);
        }
    }
}
