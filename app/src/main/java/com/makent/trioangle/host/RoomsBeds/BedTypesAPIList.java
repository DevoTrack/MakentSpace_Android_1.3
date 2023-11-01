package com.makent.trioangle.host.RoomsBeds;

import com.makent.trioangle.helper.Constants;

import java.io.Serializable;

public class BedTypesAPIList implements Serializable, BaseModel  {

        int count;

        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public int getViewType() {
            return Constants.ViewType.BedType;
        }
    }

