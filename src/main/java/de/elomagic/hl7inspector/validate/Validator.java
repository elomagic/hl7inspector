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

import java.util.ArrayList;
import java.util.List;

import de.elomagic.hl7inspector.hl7.model.*;
import de.elomagic.hl7inspector.profile.DataElement;
import de.elomagic.hl7inspector.profile.DataTypeItem;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.SegmentItem;

/**
 *
 * @author rambow
 */
public class Validator {
    private Profile profile;

    /**
     * Creates a new instance of Validator.
     */
    public Validator(Profile profile) {
        this.profile = profile;
    }

    public ValidateStatus validate(Message message) throws Exception {
        List<ValidateStatus> statusList = validateObject(message);

        // Check results
        String s = "";
        ValidateStatus status = new ValidateStatus(ValidateStatus.OK);
        for(ValidateStatus childStatus : statusList) {
            status = (status.compareTo(childStatus) > 0) ? status : childStatus;
            s = s.concat("\n".concat(childStatus.getText()));
        }

        message.setValidateStatus(status);
        message.setValidationText(s.trim().isEmpty() ? null : s.trim());

        return status;
    }

    private List<ValidateStatus> validateObject(Hl7Object obj) {
        List<ValidateStatus> statusList = new ArrayList<>();

        DataElement de = getDataElement(obj);
        String segName = getSegmentName(obj);

        if(obj instanceof Message) {
            for(int i = 0; i < obj.size(); i++) {
                statusList.addAll(validateObject(obj.get(i)));
            }
        } else if(obj instanceof Segment) {
            SegmentItem si = profile.getSegment(segName);
            if(si == null) {
                statusList.add(new ValidateStatus(profile.getValidateMapper().getMapDefNotFound(), getObjectName(obj) + ": Unknown segment."));
            }

            for(int i = 1; i < obj.size(); i++) {
                statusList.addAll(validateObject(obj.get(i)));
            }
        } else if(obj instanceof RepetitionField) {
            if(de == null) {
                statusList.add(new ValidateStatus(profile.getValidateMapper().getMapDefNotFound(), getObjectName(obj) + ": Unknown field."));
            } else {
                if(de.getRepeatableCount() < obj.size()) {
                    statusList.add(new ValidateStatus(profile.getValidateMapper().getMapRepetition(), getObjectName(obj) + ": Repetition count too large."));
                }

                for(int i = 0; i < obj.size(); i++) {
                    statusList.addAll(validateObject(obj.get(i)));
                }
            }
        } else if(obj instanceof Field) {
            if(de == null) {
                statusList.add(new ValidateStatus(profile.getValidateMapper().getMapDefNotFound(), getObjectName(obj) + ": Unknown field."));
            } else {
                int len = obj.toString().length();

                if(len > de.getLen()) {
                    statusList.add(new ValidateStatus(profile.getValidateMapper().getMapLength(), getObjectName(obj) + ": Field length too large."));
                }

                List<ValidateStatus> childStatusList = validateDataType(obj, de.getDataType());
                statusList.addAll(childStatusList);
            }
        }

        // Check results
        String s = "";
        ValidateStatus status = new ValidateStatus(ValidateStatus.OK);
        for(int i = 0; i < statusList.size(); i++) {
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

        while((obj.getHl7Parent() != null) && (!(obj.getHl7Parent() instanceof Segment))) {
            if(obj instanceof Field) {
                fieldSeq = obj.getHl7Parent().getIndex();
            }

            obj = obj.getHl7Parent();
        }

        if(obj instanceof RepetitionField) {
            String segName = obj.getHl7Parent().get(0).toString();

            if(fieldSeq == -1) {
                fieldSeq = obj.getIndex();
            }

            de = profile.getDataElement(segName, fieldSeq);
        }
        return de;
    }

    private String getObjectName(Hl7Object obj) {
        String s = "";

        while(!(obj instanceof Message)) {
            if(obj instanceof Component) {
                s = ".".concat(Integer.toString(obj.getIndex() + 1));
            } else if(obj instanceof Field) {
                s = "-".concat(Integer.toString(obj.getHl7Parent().getIndex())).concat(s);
            } else if((obj instanceof RepetitionField) && (s.indexOf('-') == -1)) {
                s = "-".concat(Integer.toString(obj.getIndex())).concat(s);
            } else if(obj instanceof Segment) {
                s = obj.get(0).toString().concat(s);
            }
            obj = obj.getHl7Parent();
        }

        return s;
    }

    private String getSegmentName(Hl7Object obj) {
        String seg = null;

        while((!(obj instanceof Message)) && (seg == null)) {
            if(obj instanceof Segment) {
                seg = obj.get(0).toString();
            }
            obj = obj.getHl7Parent();
        }

        return seg;
    }

    private List<ValidateStatus> validateDataType(Hl7Object obj, String dataType) {
        List<ValidateStatus> errorList = new ArrayList<>();

        for(int i = 0; i < obj.size(); i++) {
            try {
                Hl7Object child = obj.get(i);

                DataTypeItem dt = profile.getDataType(dataType, i + 1);
                if(dt == null) {
                    errorList.add(new ValidateStatus(profile.getValidateMapper().getMapDefNotFound(), getObjectName(child) + ": Invalid data type object."));
                    break;
                }
            } catch(Exception ex) {
                //errorList.add(e.getMessage());
            }
        }

        ValidateStatus status = new ValidateStatus(ValidateStatus.OK);
        for(int i = 0; i < errorList.size(); i++) {
            ValidateStatus childStatus = errorList.get(i);
            status = (status.compareTo(childStatus) > 0) ? status : childStatus;
        }

        obj.setValidateStatus(status);

        return errorList;
    }
}
