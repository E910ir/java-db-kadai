package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Posts_Chapter07 {

	public static void main(String[] args) {
		
		Connection con = null;
		PreparedStatement pStatement = null;
		Statement statement = null;
		
		// 投稿データ
		String[][] postData = {
				{"1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13"},
				{"1002", "2023-02-08", "お疲れ様です！", "12"},
				{"1003", "2023-02-09", "今日も頑張ります！", "18"},
				{"1001", "2023-02-09", "無理は禁物ですよ！", "17"},
				{"1002", "2023-02-10", "明日から連休ですね！", "20"}
		};
		
		try {
			// データベースに接続
			con = DriverManager.getConnection(
				"jdbc:mysql://localhost/challenge_java",
				"root",
				"Natu0918ppp!"
			);
			
			System.out.println("データベース接続成功：com.mysql.cj.jdbc.ConnectionImpl@xxxxxxxx");
			
			// SQLクエリを準備
			String insertSql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?);";
			pStatement = con.prepareStatement(insertSql);
			
			// リストの1行目から順番に読み込む
			for( int i = 0; i < postData.length; i++ ) {
				// SQLクエリの｢？｣部分をリストのデータに置き換え
				pStatement.setString(1, postData[i][0]); // ユーザーID
				pStatement.setString(2, postData[i][1]); // 投稿日時
				pStatement.setString(3, postData[i][2]); // 投稿内容
				pStatement.setString(4, postData[i][3]); // いいね数
				pStatement.executeUpdate();
			}	
				// SQLクエリを実行(DBMSに送信)
				System.out.println("レコード追加を実行します");
				System.out.println(postData.length + "件のレコードが追加されました");
			
			// SQLクエリを準備
			statement = con.createStatement();
			String selectSql = "SELECT * FROM posts  WHERE user_id = 1002;";
			
			System.out.println("ユーザーIDが1002のレコード検索しました");
				
			// SQLクエリを実行(DBMSに送信)
			ResultSet result = statement.executeQuery(selectSql);
				
			// SQLクエリの実行結果を抽出
			while(result.next()) {
				Date postedAt = result.getDate("posted_at");
				String postContent = result.getString("post_content");
				String likes = result.getString("likes");
				System.out.println(result.getRow() + "件目:投稿日時=" + postedAt + 
									"/投稿内容=" + postContent + "/いいね数=" + likes);
			}
		} catch(SQLException e) {
			System.out.println("エラー発生:" + e.getMessage());
		} finally {
			// 使用したオブジェクトを解放
			if( statement != null ) {
				try {statement.close(); } catch(SQLException ignore) {}
			}
			if( pStatement != null ) {
				try {pStatement.close(); } catch(SQLException ignore) {}
			}
			if( con != null ) {
				try { con.close(); } catch(SQLException ignore) {}
			}
		}
	}
}
