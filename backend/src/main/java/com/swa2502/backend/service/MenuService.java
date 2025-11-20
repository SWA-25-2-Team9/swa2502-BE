package com.swa2502.backend.service;

import com.swa2502.backend.domain.MenuItem;
import com.swa2502.backend.domain.MenuOption;
import com.swa2502.backend.domain.MenuOptionGroup;
import com.swa2502.backend.dto.MenuDetailDto;
import com.swa2502.backend.dto.MenuDto;
import com.swa2502.backend.dto.OptionDto;
import com.swa2502.backend.dto.OptionGroupDto;
import com.swa2502.backend.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuItemRepository menuItemRepository;

    public List<MenuDto> getMenusByShopId(Long shopId) {
        List<MenuItem> menuItems = menuItemRepository.findByShopId(shopId);
        return menuItems.stream().map(this::convertMenuToDto).collect(Collectors.toList());
    }

    public MenuDetailDto getMenuById(Long menuId) {
        MenuItem menuItem = menuItemRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + menuId));
        return convertMenuToDetailDto(menuItem);
    }

    private MenuDto convertMenuToDto(MenuItem menuItem) {
        return MenuDto.builder()
                .menuId(menuItem.getId())
                .name(menuItem.getName())
                .price(menuItem.getPrice())
                .imageUrl(menuItem.getImageUrl())
                .isSoldOut(menuItem.getIsSoldOut())
                .build();
    }

    private MenuDetailDto convertMenuToDetailDto(MenuItem menuItem) {
        List<OptionGroupDto> optionGroupDtos = menuItem.getOptionGroups().stream()
                .map(this::convertOptionGroupToDto)
                .collect(Collectors.toList());

        return MenuDetailDto.builder()
                .menuId(menuItem.getId())
                .name(menuItem.getName())
                .price(menuItem.getPrice())
                .optionGroups(optionGroupDtos)
                .build();
    }

    private OptionGroupDto convertOptionGroupToDto(MenuOptionGroup optionGroup) {
        List<OptionDto> optionDtos = optionGroup.getOptions().stream()
                .map(this::convertOptionToDto)
                .collect(Collectors.toList());

        return OptionGroupDto.builder()
                .groupId(optionGroup.getId())
                .name(optionGroup.getName())
                .required(optionGroup.getRequired())
                .min(optionGroup.getMin())
                .max(optionGroup.getMax())
                .options(optionDtos)
                .build();
    }

    private OptionDto convertOptionToDto(MenuOption option) {
        return OptionDto.builder()
                .optionId(option.getId())
                .name(option.getName())
                .extraPrice(option.getExtraPrice())
                .build();
    }
}
