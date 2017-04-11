/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.mdr.model.mapper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kovian on 10/04/2017.
 *
 * This class maps an Object (of any type) to its representation in ObjectRepresentation type or
 * in java representation : List<List<Map<Map<String, String>, String>>>
 *
 */
public class MdrGenericObjectMapper {


    public static List<ObjectRepresentation> mapToGenericObjectRepresentation(List<?> list){
        return mapToObjects(mapToJavaObjectsRepresentation(list));
    }

    private static List<ObjectRepresentation> mapToObjects(List<List<Map<Map<String, String>, String>>> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<ObjectRepresentation> entityListRepresentation = new ArrayList<>();
        for(List<Map<Map<String, String>, String>> singleEntity : entityList) {
            ObjectRepresentation entity      = new ObjectRepresentation();
            List<ColumnDataType> fields = entity.getFields();
            for(Map<Map<String, String>, String> singleField : singleEntity) {
                for(Map.Entry<Map<String, String>, String> fieldValues : singleField.entrySet()) {
                    String type = fieldValues.getValue();
                    String name = null;
                    String value = null;
                    for(Map.Entry<String, String> nameValue : fieldValues.getKey().entrySet()){
                        name = nameValue.getKey();
                        value = nameValue.getValue();
                    }
                    fields.add(new ColumnDataType(name, value, type));
                }
            }
            entityListRepresentation.add(entity);
        }
        return entityListRepresentation;
    }

    public static List<List<Map<Map<String, String>, String>>> mapToJavaObjectsRepresentation(List<?> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        // Fields of the object and parent(s) (inherited)
        Field[] declaredFields = getFieldsIncludingEnherited(entityList.get(0).getClass());
        // List of object representation
        List<List<Map<Map<String, String>, String>>> rows = new ArrayList<>();
        for(Object entity : entityList){
            // Object representation
            List<Map<Map<String, String>, String>> singleObject = new ArrayList<>();
            for (Field field : declaredFields) {
                // Reflectively read attribute Name, Value and Type
                // We don't want constants to be included here.
                if(fieldIsConstant(field)){
                    continue;
                }
                String fieldName   = field.getName();
                Class<?> fieldType = field.getType();
                field.setAccessible(true);
                Object fieldValue  = null;
                try {
                    fieldValue = field.get(entity);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                Map<Map<String, String>, String> fieldNameValType = new HashMap<>();
                Map<String, String> fieldNameVal = new HashMap<>();
                fieldNameVal.put(fieldName, fieldValue != null ? fieldValue.toString() : null);
                fieldNameValType.put(fieldNameVal, fieldType.toString());
                singleObject.add(fieldNameValType);
            }
            rows.add(singleObject);
        }
        return rows;
    }

    private static boolean fieldIsConstant(Field field) {
        if(Modifier.isStatic(field.getModifiers())){
            return true;
        }
        return false;
    }

    private static Field[] getFieldsIncludingEnherited(Class<?> entityClass) {
        // Fields of the object and parent(s).
        Field[] declaredFields = entityClass.getDeclaredFields();
        if (entityClass.getSuperclass() != null) {
            Field[] parentFields = getFieldsIncludingEnherited(entityClass.getSuperclass());
            declaredFields = ArrayUtils.addAll(declaredFields, parentFields);
        }
        return declaredFields;
    }


}
