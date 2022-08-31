package kim.nzxy.robin.util;

import java.util.Collection;

/**
 * @author xy
 * @since 2021/6/4
 */
public class MatcherUtil {
    /**
     * '?' 可以匹配任何单个字符。
     * '* ' 可以匹配任意字符串（包括空字符串）。
     *
     * @param str     可能为空，且只包含从 a-z 的小写字母。
     * @param pattern 可能为空，且只包含从 a-z 的小写字母，以及字符 ? 和 *。
     * @return 为 true 表示匹配
     */
    public static boolean str(String str, String pattern) {
        int m = str.length();
        int n = pattern.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int i = 1; i <= n; ++i) {
            if (pattern.charAt(i - 1) == '*') {
                dp[0][i] = true;
            } else {
                break;
            }
        }
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (pattern.charAt(j - 1) == '*') {
                    dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                } else if (pattern.charAt(j - 1) == '?' || str.charAt(i - 1) == pattern.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
            }
        }
        return dp[m][n];
    }

    /**
     * @see #str(String, String)
     */
    public static boolean str(String str, Collection<String> patterns) {
        for (String pattern : patterns) {
            if (str(str,pattern)) {
                return true;
            }
        }
        return false;
    }
}
