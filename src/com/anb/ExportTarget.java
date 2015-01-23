package com.anb;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *  Date: 1/12/15
 * Time: 11:54 PM
 * To change this template use File | Settings | File Templates.
 */


    public interface ExportTarget
    {
        /**
         * Begin the export process.
         *
         * @param properties
         */
        void begin(AttributeInfo[] properties);

        /**
         * Write a group of data related to a specific object to be exported.
         *
         * @param properties
         *      the properties of the object that will be exported.
         * @param data
         *      the current data of the object, each entry represents a property value of the object
         * @return
         */
        boolean writeData(AttributeInfo[] properties, Map data);

        /**
         * Ends the export process, good place to close resources of the underlying store mechanism.
         */
        void end();
    }

