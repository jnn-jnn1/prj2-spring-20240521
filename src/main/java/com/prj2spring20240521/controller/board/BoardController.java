package com.prj2spring20240521.controller.board;

import com.prj2spring20240521.domain.board.Board;
import com.prj2spring20240521.domain.member.Member;
import com.prj2spring20240521.service.board.BoardService;
import com.prj2spring20240521.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;
    private final MemberService memberService;

    @PostMapping("add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity add(
            Authentication authentication,
            @RequestBody Board board) {
        if (service.validate(board)) {
            service.add(board, authentication);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("list")
    public List<Board> list() {
        return service.list();
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable Integer id) {
        Board board = service.get(id);
        if (board == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(board);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@RequestBody Member member, Authentication authentication) {
        if (memberService.hasAccess(member, authentication)) {
            service.remove(member.getId());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("edit")
    public ResponseEntity edit(@RequestBody Board board) {
        if (service.validate(board)) {
            service.edit(board);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
