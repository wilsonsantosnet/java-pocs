// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    @GetMapping("/webapiA")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_AccessAPI')")
    public String file() {

        //var teste = "";

        return "Response from webApiA.";
    }

 

}
