package cn.edu.nju.poem;

/**     
 * 类名称：OnePoem    
 * 类描述：      
 *     
 */
public class OnePoem {
	private String h1;
	private String h2;
	private String h3;
	private String content;

	public OnePoem(String h1, String h2, String h3, String content) {
		this.h1 = h1;
		this.h2 = h2;
		this.h3 = h3;
		this.content = content;
	}

	public String getH1() {
		return h1;
	}

	public void setH1(String h1) {
		this.h1 = h1;
	}

	public String getH2() {
		return h2;
	}

	public void setH2(String h2) {
		this.h2 = h2;
	}

	public String getH3() {
		return h3;
	}

	public void setH3(String h3) {
		this.h3 = h3;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
