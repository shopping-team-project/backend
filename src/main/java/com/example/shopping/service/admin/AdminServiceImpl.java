package com.example.shopping.service.admin;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.entity.board.BoardEntity;
import com.example.shopping.entity.board.BoardImgEntity;
import com.example.shopping.entity.comment.CommentEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.item.ItemImgEntity;
import com.example.shopping.repository.board.BoardImgRepository;
import com.example.shopping.repository.board.BoardRepository;
import com.example.shopping.repository.comment.CommentRepository;
import com.example.shopping.repository.item.ItemImgRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.service.s3.S3ItemImgUploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    // 상품 관련
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final S3ItemImgUploaderService s3ItemImgUploaderService;
    // 게시글 관련
    private final BoardRepository boardRepository;
    private final BoardImgRepository boardImgRepository;
    // 댓글 관련
    private final CommentRepository commentRepository;

    // 상품 삭제
    @Override
    public String removeItem(Long itemId, UserDetails userDetails) {
        try {
            // 삭제할 권한이 있는지 확인
            // userDetails에서 권한을 가져오기
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            // GrantedAuthority 타입의 권한을 List<String>으로 담아줌
            List<String> collectAuthorities = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // 상품 조회
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);
            // 상품 이미지 조회
            List<ItemImgEntity> findItemImg = itemImgRepository.findByItemItemId(itemId);

            // 권한이 있는지 체크
            if (!collectAuthorities.isEmpty()) {
                for (String role : collectAuthorities) {
                    // 존재하는 권한이 관리자인지 체크
                    if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                        // 삭제하는데 이미지를 풀어놓는 이유는
                        // S3에 삭제할 때 넘겨줘야 할 매개변수때문이다.
                        for (ItemImgEntity itemImgEntity : findItemImg) {
                            String uploadImgPath = itemImgEntity.getUploadImgPath();
                            String uploadImgName = itemImgEntity.getUploadImgName();

                            // 상품 정보 삭제
                            itemRepository.deleteByItemId(findItem.getItemId());
                            // S3에서 이미지 삭제
                            s3ItemImgUploaderService.deleteFile(uploadImgPath, uploadImgName);
                            return "상품을 삭제 했습니다.";
                        }
                    }
                }
            }
            return "상품 삭제 권한이 없습니다.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // 게시물 삭제
    @Override
    public String removeBoard(Long boardId, UserDetails userDetails) {
        try {
            // 삭제할 권한이 있는지 확인
            // userDetails에서 권한을 가져오기
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            // GrantedAuthority 타입의 권한을 List<String>으로 담아줌
            List<String> collectAuthorities = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // 게시글 조회
            BoardEntity findBoard = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);
            // 게시글 이미지 조회
            List<BoardImgEntity> findBoardImg = boardImgRepository.findByBoardBoardId(boardId);

            // 권한이 있는지 체크
            if (!collectAuthorities.isEmpty()) {
                for (String role : collectAuthorities) {
                    // 존재하는 권한이 관리자인지 체크
                    if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                        // 삭제하는데 이미지를 풀어놓는 이유는
                        // S3에 삭제할 때 넘겨줘야 할 매개변수때문이다.
                        for (BoardImgEntity boardImgEntity : findBoardImg) {
                            String uploadImgPath = boardImgEntity.getUploadImgPath();
                            String uploadImgName = boardImgEntity.getUploadImgName();

                            // 게시글 정보 삭제
                            boardRepository.deleteByBoardId(findBoard.getBoardId());
                            // S3에서 이미지 삭제
                            return "상품을 삭제 했습니다.";
                        }
                    }
                }
            }
            return "게시글 삭제 권한이 없습니다.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // 댓글 삭제
    @Override
    public String removeComment(Long boardId, Long commentId, UserDetails userDetails) {
        try {
            // 삭제할 권한이 있는지 확인
            // userDetails에서 권한을 가져오기
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            // GrantedAuthority 타입의 권한을 List<String>으로 담아줌
            List<String> collectAuthorities = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // 게시글 조회
            BoardEntity findBoard = boardRepository.deleteByBoardId(boardId);

            // 댓글 조회
            CommentEntity findComment = commentRepository.findById(commentId)
                    .orElseThrow(EntityNotFoundException::new);

            // 권한이 있는지 체크
            if (!collectAuthorities.isEmpty()) {
                for (String role : collectAuthorities) {
                    // 존재하는 권한이 관리자인지 체크
                    if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                        // 댓글에 담긴 게시글의 아이디와 게시글 자체 아이디가 일치할 때 true
                        if(findComment.getBoard().getBoardId().equals(findBoard.getBoardId())) {
                            commentRepository.deleteById(findComment.getCommentId());
                            return "댓글을 삭제했습니다.";
                        }
                    }
                }
            }
            return "댓글 삭제 권한이 없습니다.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    // 상품 전체 페이지 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ItemDTO> superitendItem(Pageable pageable,
                                                  UserDetails userDetails,
                                                  ItemSellStatus itemSellStatus) {
        try {
            // userDetails에서 권한을 가져오기
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            // GrantedAuthority 타입의 권한을 List<String>으로 담아줌
            List<String> collectAuthorities = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            for (String role : collectAuthorities) {
                // 존재하는 권한이 관리자인지 체크
                if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                    // 페이지 처리해서 예약된 것만 조회
                    Page<ItemEntity> items =
                            itemRepository.findByItemSellStatus(pageable, itemSellStatus);
                    log.info("items : {}", items);
                    return items.map(ItemDTO::toItemDTO);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 상품 상세정보
    // 상품의 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정합니다.
    // 이럴 경우 JPA가 더티체킹(변경감지)를 수행하지 않아서 성능을 향상 시킬 수 있다.
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<ItemDTO> getItem(Long itemId, UserDetails userDetails) {
        try {
            // userDetails에서 권한을 가져오기
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            // GrantedAuthority 타입의 권한을 List<String>으로 담아줌
            List<String> collectAuthorities = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // 상품 조회
            ItemEntity findItem = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);

            for (String role : collectAuthorities) {
                // 존재하는 권한이 관리자인지 체크
                if (role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
                    ItemDTO itemDTO = ItemDTO.toItemDTO(findItem);
                    return ResponseEntity.ok().body(itemDTO);
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
