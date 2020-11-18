/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.mdr.model.mapper;

import java.math.BigInteger;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.mdr.communication.*;


/**
 * Created by kovian on 06/12/2016.
 */
public class MdrModuleMapper {

    public static SetFLUXMDRSyncMessageResponse createFluxMdrSyncEntityRequest(String request){
        if (StringUtils.isEmpty(request)) {
            return null;
        }
        SetFLUXMDRSyncMessageResponse response = new SetFLUXMDRSyncMessageResponse();
        response.setMethod(MdrModuleMethod.SYNC_MDR_CODE_LIST);
        response.setRequest(request);
        return response;
    }

    public static MdrGetCodeListRequest createFluxMdrGetCodeListRequest(String acronym) {
        return createFluxMdrGetCodeListRequest(acronym, null, null, null);
    }

    public static MdrGetCodeListRequest createFluxMdrGetCodeListRequest(String acronym, String filter, List<String> columns, Integer nrOfResults) {
        if (StringUtils.isEmpty(acronym)) {
            return null;
        }
        MdrGetCodeListRequest request = new MdrGetCodeListRequest();
        request.setAcronym(acronym);
        request.setFilter(filter);
        request.setColumnsToFilters(columns);
        BigInteger ndrOfRes = nrOfResults != null ? BigInteger.valueOf(nrOfResults) : null;
        request.setWantedNumberOfResults(ndrOfRes);
        request.setMethod(MdrModuleMethod.GET_MDR_CODE_LIST);
        return request;
    }

    public static MdrGetCodeListResponse createFluxMdrGetCodeListResponse(List<?> codelistList, String acronym, ValidationResultType valid, String message)  {
        MdrGetCodeListResponse response = new MdrGetCodeListResponse();
        response.setAcronym(acronym);
        response.setValidation(new ValidationResult(valid, message));
        List<ObjectRepresentation> objectRepresentations = null;
        if (CollectionUtils.isNotEmpty(codelistList)) {
            objectRepresentations = MdrGenericObjectMapper.mapToGenericObjectRepresentation(codelistList);
        }
        response.setDataSets(objectRepresentations);
        response.setMethod(MdrModuleMethod.MDR_CODE_LIST_RESP);
        return response;
    }

    public static MdrGetCodeListResponse createFluxMdrGetCodeListResponse(List<?> codelistList, ValidationResultType valid, String message)  {
        MdrGetCodeListResponse response = new MdrGetCodeListResponse();
        response.setValidation(new ValidationResult(valid, message));
        List<ObjectRepresentation> objectRepresentations = null;
        if (CollectionUtils.isNotEmpty(codelistList)) {
            objectRepresentations = MdrGenericObjectMapper.mapToGenericObjectRepresentation(codelistList);
        }
        response.setDataSets(objectRepresentations);
        response.setMethod(MdrModuleMethod.MDR_CODE_LIST_RESP);
        return response;
    }

    public static SingleCodeListRappresentation mapToSingleCodeListRappresentation(List<?> codelistList, String acronym, ValidationResultType valid, String message)  {
        SingleCodeListRappresentation singleObjRappr = new SingleCodeListRappresentation();
        singleObjRappr.setAcronym(acronym);
        singleObjRappr.setValidation(new ValidationResult(valid, message));
        List<ObjectRepresentation> objectRepresentations = null;
        if (CollectionUtils.isNotEmpty(codelistList)) {
            objectRepresentations = MdrGenericObjectMapper.mapToGenericObjectRepresentation(codelistList);
        }
        singleObjRappr.setDataSets(objectRepresentations);
        return singleObjRappr;
    }

}
