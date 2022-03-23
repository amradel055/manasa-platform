/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.complete;

import com.isag.dto.RscWorkUnitsGuideDTO;
import java.util.List;

/**
 *
 * @author Mostafa
 */
public interface autoCompleteService {
     public List<RscWorkUnitsGuideDTO> completeWorkUnitsGuideAsUnit (String query);
}
