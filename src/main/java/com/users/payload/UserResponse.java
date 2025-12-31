package com.users.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    public List<UserDTO> content;
    public int pageNumber;
    public int pageSize;
    public long totalElements;
    public int totalPages;
    public boolean lastPage;
}
