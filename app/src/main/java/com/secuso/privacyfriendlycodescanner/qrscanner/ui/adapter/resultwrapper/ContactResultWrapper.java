package com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.resultwrapper;

import com.google.zxing.client.result.AddressBookParsedResult;

/**
 * Wrapper for the Contact Result class. Adds some utility functions used by the
 * {@link com.secuso.privacyfriendlycodescanner.qrscanner.ui.adapter.ContactResultAdapter}
 *
 * @author Christopher Beckmann
 */
public class ContactResultWrapper {

    private final AddressBookParsedResult result;

    public ContactResultWrapper(AddressBookParsedResult result) {
        this.result = result;
    }

    public AddressBookParsedResult getResult() {
        return result;
    }

    public int getDataCount() {
        int itemCount = 0;

        if(hasNames() || hasNickNames() || hasPronunciation())  itemCount ++;
                                                                itemCount += getPhoneNumberCount();
                                                                itemCount += getEmailCount();
        if(hasInstantMessenger())                               itemCount ++;
        if(hasNote())                                           itemCount ++;
                                                                itemCount += getAddressCount();
        if(hasOrganisation())                                   itemCount ++;
        if(hasBirthday())                                       itemCount ++;
        if(hasTitle())                                          itemCount ++;
                                                                itemCount += getUrlCount();
                                                                itemCount += getGeoCount();

        return itemCount;
    }

    public int getDataIndex(int position) {
        int itemCount = 0;

        if(hasNames() || hasNickNames() || hasPronunciation())  itemCount++;
        if(position < itemCount) {
            return itemCount - position - 1;
        }

        itemCount += getPhoneNumberCount();
        if(position < itemCount) {
            return itemCount - position - 1;
        }

        itemCount += getEmailCount();
        if(position < itemCount) {
            return itemCount - position - 1;
        }

        if(hasInstantMessenger()) itemCount ++;
        if(position < itemCount) {
            return itemCount - position - 1;
        }

        if(hasNote()) itemCount ++;
        if(position < itemCount) {
            return itemCount - position - 1;
        }

        itemCount += getAddressCount();
        if(position < itemCount) {
            return itemCount - position - 1;
        }

        if(hasOrganisation()) itemCount ++;
        if(position < itemCount) {
            return itemCount - position - 1;
        }

        if(hasBirthday()) itemCount ++;
        if(position < itemCount) {
            return itemCount - position - 1;
        }

        if(hasTitle()) itemCount ++;
        if(position < itemCount) {
            return itemCount - position - 1;
        }

        itemCount += getUrlCount();
        if(position < itemCount) {
            return itemCount - position - 1;
        }

        itemCount += getGeoCount();
        if(position < itemCount) {
            return itemCount - position - 1;
        }

        return 0;
    }

    public String getName() {
        StringBuilder sb = new StringBuilder();

        append(result.getNames(), sb);
        append(result.getNicknames(), sb);

        return sb.toString();
    }

    public String getPronunciation() {
        if(hasPronunciation() && !result.getPronunciation().isEmpty()) {
            return result.getPronunciation();
        }
        return "";
    }

    public String getAddressType(int index) {
        if(hasAddressType(index)) {
            return result.getAddressTypes()[index];
        }
        return "";
    }

    public String getPhoneNumber(int index) {
        if(hasPhoneNumbers() && result.getPhoneNumbers().length > 0) {
            return result.getPhoneNumbers()[index];
        }
        return "";
    }

    public String getUrl(int index) {
        if(hasUrls() && result.getURLs().length > 0) {
            return result.getURLs()[index];
        }
        return "";
    }
    public String getGeo(int index) {
        if(hasGeo() && result.getGeo().length > 0) {
            return result.getGeo()[index];
        }
        return "";
    }

    public String getEmail(int index) {
        if(hasEmails() && result.getEmails().length > 0) {
            return result.getEmails()[index];
        }
        return "";
    }

    public String getEmailType(int index) {
        if(hasEmailType(index)) {
            return result.getEmailTypes()[index];
        }
        return "";
    }

    public boolean hasEmailType(int index) {
        return (hasEmailTypes() && result.getEmailTypes().length > 0 && index < result.getEmailTypes().length);
    }

    public boolean hasAddressType(int index) {
        return (hasAddressTypes() && result.getAddressTypes().length > 0 && index < result.getAddressTypes().length);
    }

    public String getPhoneNumberType(int index) {
        if(hasPhoneNumberType(index)) {
            return result.getPhoneTypes()[index];
        }
        return "";
    }

    public boolean hasPhoneNumberType(int index) {
        return result.getPhoneTypes() != null && result.getPhoneTypes().length > 0 && index < result.getPhoneTypes().length;
    }

    public String getAddress(int index) {
        if(hasAddresses() && result.getAddresses().length > 0) {
            return result.getAddresses()[index];
        }
        return "";
    }

    public int getNameCount() {
        return hasNames() ? result.getNames().length : 0;
    }

    public int getNicknameCount() {
        return hasNickNames() ? result.getNicknames().length : 0;
    }

    public int getPhoneNumberCount() {
        return hasPhoneNumbers() ? result.getPhoneNumbers().length : 0;
    }

    public int getEmailCount() {
        return hasEmails() ? result.getEmails().length : 0;
    }

    public int getAddressCount() {
        return hasAddresses() ? result.getAddresses().length : 0;
    }

    public int getGeoCount() {
        return hasGeo() ? result.getGeo().length : 0;
    }

    public int getUrlCount() {
        return hasUrls() ? result.getURLs().length : 0;
    }

    public boolean hasNames() {
        return result.getNames() != null;
    }
    public boolean hasNickNames() {
        return result.getNicknames() != null;
    }
    public boolean hasPronunciation() {
        return result.getPronunciation() != null;
    }
    public boolean hasPhoneNumbers() {
        return result.getPhoneNumbers() != null;
    }
    public boolean hasPhoneTypes() {
        return result.getPhoneTypes() != null;
    }
    public boolean hasEmails() {
        return result.getEmails() != null;
    }
    public boolean hasEmailTypes() {
        return result.getEmailTypes() != null;
    }
    public boolean hasInstantMessenger() {
        return result.getInstantMessenger() != null;
    }
    public boolean hasNote() {
        return result.getNote() != null;
    }
    public boolean hasAddresses() {
        return result.getAddresses() != null;
    }
    public boolean hasAddressTypes() {
        return result.getAddressTypes() != null;
    }
    public boolean hasOrganisation() {
        return result.getOrg() != null;
    }
    public boolean hasBirthday() {
        return result.getBirthday() != null;
    }
    public boolean hasTitle() {
        return result.getTitle() != null;
    }
    public boolean hasUrls() {
        return result.getURLs() != null;
    }
    public boolean hasGeo() {
        return result.getGeo() != null;
    }

    void append(String value, StringBuilder sb) {
        if(value != null) {
            if(sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(value);
        }
    }

    void append(String[] values, StringBuilder sb) {
        if(values != null) {
            for(String value : values) {
                append(value, sb);
            }
        }
    }
}
