package com.webstore.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @Schema(description = "Category ID", example = "111")
    private Long categoryId;
    @Schema(description = "Category name for category you want to create", example = "iphone 14")
    private String categoryName;
}
