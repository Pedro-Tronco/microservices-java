package br.edu.atitus.inventory_service.entities;

import java.util.Objects;
import java.util.UUID;

public class BookmarksId {

	private Long userId;
	
	private UUID bookmarkId;
	
	public BookmarksId() {}
	
	public BookmarksId(Long userId, UUID bookmarkId) {
		this.userId = userId;
		this.bookmarkId = bookmarkId;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookmarksId that = (BookmarksId) o;
        return Objects.equals(userId, that.userId) &&
        		Objects.equals(bookmarkId, that.bookmarkId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookmarkId);
    }
	
}
