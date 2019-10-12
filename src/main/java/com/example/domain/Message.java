package com.example.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

@Entity
public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2182938266636318057L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NotBlank(message = "Please fill the message")
	@Length(max=2048, message = "Message too long (more than 2 kB)")
	private String text;
	
	@Length(max=255, message = "Message too long (more than 255)")
	private String tag;
	
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
	private User author;
	
    
    private String filename;
	
	public String getAuthorName() {
		return author != null ? author.getUsername() : "<none>";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Message(String text, String tag, User author) {
		this.text = text;
		this.tag = tag;
		this.author = author;
	}
	public Message( String text, String tag) {
	
		this.text = text;
		this.tag = tag;
	}
	public Message() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	

}
