/*
 * Copyright 2006 Carsten Rambow
 * 
 * Licensed under the GNU Public License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.gnu.org/licenses/gpl.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.elomagic.hl7inspector.validate;

import de.elomagic.hl7inspector.hl7.model.*;
import de.elomagic.hl7inspector.profile.DataElement;
import de.elomagic.hl7inspector.profile.DataTypeItem;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.SegmentItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rambow
 */
public class Validator {

    /** Creates a new instance of Validator */
    public Validator(Profile profile) {
        this.profile = profile;
    }

    private Profile profile;

    public ValidateStatus validate(Message message) throws Exception {
        List<ValidateStatus> statusList = validateObject(message);

        // Check results
        String s = "";
        ValidateStatus status = new ValidateStatus(ValidateStatus.OK);
        for (int i = 0; i < statusList.size(); i++) {
            ValidateStatus childStatus = statusList.get(i);
            status = (status.compareTo(childStatus) > 0) ? status : childStatus;
            s = s.concat("\n".concat(statusList.get(i).getText()));
        }

        message.setValidateStatus(status);
        message.setValidationText(s.trim().length() == 0 ? null : s.trim());

        return status;
    }

    private List<ValidateStatus> validateObject(Hl7Object obj) {
        List<ValidateStatus> statusList = new ArrayList<ValidateStatus>();

        DataElement de = getDataElement(obj);
        String segName = getSegmentName(obj);

        if (obj instanceof Message) {
            for (int i = 0; i < obj.size(); i++) {
                statusList.addAll(validateObject(obj.get(i)));
            }
        } else if (obj instanceof Segment) {
            SegmentItem si = profile.getSegment(segName);
            if (si == null) {
                statusList.add(new ValidateStatus(profile.getValidateMapper().getMapDefNotFound(), getObjectName(obj) + ": Unknown segment."));
            }

            for (int i = 1; i < obj.size(); i++) {
                statusList.addAll(validateObject(obj.get(i)));
            }
        } else if (obj instanceof RepetitionField) {
            if (de == null) {
                statusList.add(new ValidateStatus(profile.getValidateMapper().getMapDefNotFound(), getObjectName(obj) + ": Unknown field."));
            } else {
                if (de.getRepeatableCount() < obj.size()) {
                    statusList.add(new ValidateStatus(profile.getValidateMapper().getMapRepetition(), getObjectName(obj) + ": Repetition count too large."));
                }

                for (int i = 0; i < obj.size(); i++) {
                    statusList.addAll(validateObject(obj.get(i)));
                }
            }
        } else if (obj instanceof Field) {
            if (de == null) {
                statusList.add(new ValidateStatus(profile.getValidateMapper().getMapDefNotFound(), getObjectName(obj) + ": Unknown field."));
            } else {
                int len = obj.toString().length();

                if (len > de.getLen()) {
                    statusList.add(new ValidateStatus(profile.getValidateMapper().getMapLength(), getObjectName(obj) + ": Field length too large."));
                }

                List<ValidateStatus> childStatusList = validateDataType(obj, de.getDataType());
                statusList.addAll(childStatusList);
            }
        }

        // Check results
        String s = "";
        ValidateStatus status = new ValidateStatus(ValidateStatus.OK);
        for (int i = 0; i < statusList.size(); i++) {
            ValidateStatus childStatus = statusList.get(i);
            status = (status.compareTo(childStatus) > 0) ? status : childStatus;
            s = s.concat("\n".concat(statusList.get(i).getText()));
        }

        obj.setValidateStatus(status);
        obj.setValidationText(s.trim().length() == 0 ? null : s.trim());

        return statusList;
    }

    private DataElement getDataElement(Hl7Object obj) {
        int fieldSeq = -1;
        DataElement de = null;

        while ((obj.getHl7Parent() != null) && (!(obj.getHl7Parent() instanceof Segment))) {
            if (obj instanceof Field) {
                fieldSeq = obj.getHl7Parent().getIndex();
            }

            obj = obj.getHl7Parent();
        }

        if (obj instanceof RepetitionField) {
            String segName = obj.getHl7Parent().get(0).toString();

            if (fieldSeq == -1) {
                fieldSeq = obj.getIndex();
            }

            de = profile.getDataElement(segName, fieldSeq);
        }
        return de;
    }

    private String getObjectName(Hl7Object obj) {
        String s = "";

        while (!(obj instanceof Message)) {
            if (obj instanceof Component) {
                s = ".".concat(Integer.toString(obj.getIndex() + 1));
            } else if (obj instanceof Field) {
                s = "-".concat(Integer.toString(obj.getHl7Parent().getIndex())).concat(s);
            } else if ((obj instanceof RepetitionField) && (s.indexOf('-') == -1)) {
                s = "-".concat(Integer.toString(obj.getIndex())).concat(s);
            } else if (obj instanceof Segment) {
                s = obj.get(0).toString().concat(s);
            }
            obj = obj.getHl7Parent();
        }

        return s;
    }

    private String getSegmentName(Hl7Object obj) {
        String seg = null;

        while ((!(obj instanceof Message)) && (seg == null)) {
            if (obj instanceof Segment) {
                seg = obj.get(0).toString();
            }
            obj = obj.getHl7Parent();
        }

        return seg;
    }

    private List<ValidateStatus> validateDataType(Hl7Object obj, String dataType) {
        List<ValidateStatus> errorList = new ArrayList<ValidateStatus>();

        for (int i = 0; i < obj.size(); i++) {
            try {
                Hl7Object child = obj.get(i);

                DataTypeItem dt = profile.getDataType(dataType, i + 1);
                if (dt == null) {
                    errorList.add(new ValidateStatus(profile.getValidateMapper().getMapDefNotFound(), getObjectName(child) + ": Invalid data type object."));
                    break;
                }
            } catch (Exception ex) {
                //errorList.add(e.getMessage());                
            }
        }

        ValidateStatus status = new ValidateStatus(ValidateStatus.OK);
        for (int i = 0; i < errorList.size(); i++) {
            ValidateStatus childStatus = errorList.get(i);
            status = (status.compareTo(childStatus) > 0) ? status : childStatus;
        }

        obj.setValidateStatus(status);

        return errorList;
    }

    private static String ILLEGAL_DATA_TYPE_FORMAT_TEXT = "Illegal data type format";
    // Simple data type = DT, DTM, FT, GTS, ID, IS, NM, SI ST, TM, TX    

    private ValidateStatus validateDataTypeDT(Hl7Object obj) {
        ValidateStatus status = null;

        try {
            if (obj.size() > 1) {
                throw new Exception(ILLEGAL_DATA_TYPE_FORMAT_TEXT);
            }

            int len = obj.toString().length();
            String pat = "";

            switch (len) {
                case 4:
                    pat = "yyyy";
                    break;
                case 6:
                    pat = "yyyyMM";
                    break;
                case 8:
                    pat = "yyyyMMdd";
                    break;
                default:
                    throw new Exception(ILLEGAL_DATA_TYPE_FORMAT_TEXT);
            }

            SimpleDateFormat sdf = new SimpleDateFormat(pat);

            sdf.parse(obj.toString());
        } catch (Exception e) {
        }

        return status;
    }

    //* Validate Hl7 object against data type TM
    private ValidateStatus validateDataTypeDTM(Hl7Object obj) {
        ValidateStatus status = null;

        try {
            if (obj.size() > 1) {
                throw new Exception(ILLEGAL_DATA_TYPE_FORMAT_TEXT);
            }

            String pat = "";
            String patTz = "";
            String s = obj.toString();
            int len = s.toString().length();

            // If timezone set ...
            if ((s.indexOf('-') != -1) || (s.indexOf('+') != -1)) {
                int i = (s.indexOf('-') != -1) ? s.indexOf('-') : s.indexOf('+');
                len = s.substring(0, i).length();
                patTz = "Z";

                if (s.substring(i + 1).length() != 4) {
                    throw new Exception(ILLEGAL_DATA_TYPE_FORMAT_TEXT);
                }
            }

            switch (len) {
                case 4:
                    pat = "yyyy";
                    break;
                case 6:
                    pat = "yyyyMM";
                    break;
                case 8:
                    pat = "yyyyMMdd";
                    break;
                case 10:
                    pat = "yyyyMMddHH";
                    break;
                case 12:
                    pat = "yyyyMMddHHmm";
                    break;
                case 14:
                    pat = "yyyyMMddHHmmss";
                    break;
                case 16:
                    pat = "yyyyMMddHHmmss.S";
                    break;
                case 17:
                    pat = "yyyyMMddHHmmss.SS";
                    break;
                case 18:
                    pat = "yyyyMMddHHmmss.SSS";
                    break;
                case 19:
                    pat = "yyyyMMddHHmmss.SSSS";
                    break;
                default:
                    throw new Exception(ILLEGAL_DATA_TYPE_FORMAT_TEXT);
            }

            SimpleDateFormat sdf = new SimpleDateFormat(pat.concat(patTz));

            sdf.parse(s);
        } catch (Exception e) {
        }

        return status;
    }

    private ValidateStatus validateDataTypeNM(Hl7Object obj) {
        ValidateStatus status = null;

        try {
            if (obj.size() > 1) {
                throw new Exception(ILLEGAL_DATA_TYPE_FORMAT_TEXT);
            }

            Long.parseLong(obj.toString());
        } catch (Exception e) {
        }

        return status;
    }

    private ValidateStatus validateDataTypeSI(Hl7Object obj) {
        ValidateStatus status = null;

        try {
            if (obj.size() > 1) {
                throw new Exception(ILLEGAL_DATA_TYPE_FORMAT_TEXT);
            }

            if (Long.parseLong(obj.toString()) < 0) {
                throw new Exception(ILLEGAL_DATA_TYPE_FORMAT_TEXT);
            }
        } catch (Exception e) {
        }

        return status;
    }

    private ValidateStatus validateDataTypeTM(Hl7Object obj) {
        ValidateStatus status = null;

        try {
            if (obj.size() > 1) {
                throw new Exception(ILLEGAL_DATA_TYPE_FORMAT_TEXT);
            }

            String pat = "";
            String patTz = "";
            String s = obj.toString();
            int len = s.toString().length();

            // If timezone set ...
            if ((s.indexOf('-') != -1) || (s.indexOf('+') != -1)) {
                int i = (s.indexOf('-') != -1) ? s.indexOf('-') : s.indexOf('+');
                len = s.substring(0, i).length();
                patTz = "Z";

                if (s.substring(i + 1).length() != 4) {
                    throw new Exception(ILLEGAL_DATA_TYPE_FORMAT_TEXT);
                }
            }

            switch (len) {
                case 2:
                    pat = "HH";
                    break;
                case 4:
                    pat = "HHmm";
                    break;
                case 6:
                    pat = "HHmmss";
                    break;
                case 8:
                    pat = "HHmmss.S";
                    break;
                case 9:
                    pat = "HHmmss.SS";
                    break;
                case 10:
                    pat = "HHmmss.SSS";
                    break;
                case 11:
                    pat = "HHmmss.SSSS";
                    break;
                default:
                    throw new Exception(ILLEGAL_DATA_TYPE_FORMAT_TEXT);
            }

            SimpleDateFormat sdf = new SimpleDateFormat(pat.concat(patTz));

            sdf.parse(s);
        } catch (Exception e) {
        }

        return status;
    }

}
