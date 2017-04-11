/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.mdr.model.mapper;

import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.mdr.communication.*;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by kovian on 06/12/2016.
 */
public class MdrModuleMapper {

    public static String createFluxMdrSyncEntityRequest(String request, String empty) throws MdrModelMarshallException {
        if (StringUtils.isEmpty(request)) {
            return null;
        }
        SetFLUXMDRSyncMessageResponse response = new SetFLUXMDRSyncMessageResponse();
        response.setMethod(MdrModuleMethod.SYNC_MDR_CODE_LIST);
        response.setRequest(request);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String createFluxMdrGetCodeListRequest(String acronym, String filter, List<String> columns, Integer nrOfResults) throws MdrModelMarshallException {
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
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String createFluxMdrGetCodeListResponse(List<?> codelistList, String acronym, ValidationResultType valid, String message) throws MdrModelMarshallException {
        MdrGetCodeListResponse response = new MdrGetCodeListResponse();
        response.setAcronym(acronym);
        response.setValidation(new ValidationResult(valid, message));
        List<ObjectRepresentation> objectRepresentations = null;
        if (CollectionUtils.isNotEmpty(codelistList)) {
            objectRepresentations = MdrGenericObjectMapper.mapToGenericObjectRepresentation(codelistList);
        }
        response.setDataSets(objectRepresentations);
        response.setMethod(MdrModuleMethod.MDR_CODE_LIST_RESP);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

    public static String createFluxMdrGetCodeListErrorResponse(String errorMessage) throws MdrModelMarshallException {
        MdrGetCodeListResponse response = new MdrGetCodeListResponse();
        response.setAcronym(null);
        response.setValidation(new ValidationResult(ValidationResultType.NOK, errorMessage));
        response.setDataSets(null);
        response.setMethod(MdrModuleMethod.MDR_CODE_LIST_RESP);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }


}
