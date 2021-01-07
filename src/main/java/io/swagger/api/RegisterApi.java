/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.19).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.RegisterRequest;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-03T10:32:36.707Z[GMT]")
@Api(value = "register", description = "the register API")
public interface RegisterApi {

    @ApiOperation(value = "Registers user by starting user sign up request", nickname = "registerUser", notes = "Registers user by starting user sign up request, if the employee accepts the request the user will be created", tags={ "Authentication","Customer operation", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "You have succesfully created your account."),
        @ApiResponse(code = 400, message = "Something went wrong, your request could not be understood. Please try to register again."),
        @ApiResponse(code = 401, message = "Something went wrong when we tried to create your account, please try again."),
        @ApiResponse(code = 403, message = "You are not allowed to create an account."),
        @ApiResponse(code = 404, message = "The page that you are trying to enter does not exist anymore."),
        @ApiResponse(code = 406, message = "Something went wrong with your credentials, please try again."),
        @ApiResponse(code = 429, message = "You have tried to create an account too many times.") })
    @RequestMapping(value = "/register",
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<RegisterRequest> registerUser(@ApiParam(value = ""  )  @Valid @RequestBody RegisterRequest body
);

}