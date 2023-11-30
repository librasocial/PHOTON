package com.ssf.abdm.model.sendResponse.shareprofile;

import com.ssf.abdm.model.sendResponse.Common;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Share extends Common {
    private Acknowledgement acknowledgement;
}

