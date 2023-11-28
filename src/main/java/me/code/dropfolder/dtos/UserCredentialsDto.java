package me.code.dropfolder.dtos;

/**
 * Record representing the data transfer object (DTO) for user credentials.
 *
 * @param username The username associated with the user.
 * @param password The password associated with the user.
 */
public record UserCredentialsDto(String username, String password) {
}
