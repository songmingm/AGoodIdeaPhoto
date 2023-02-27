import { BugOutlined, GithubOutlined, UserOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';

/**
 * 全局 Footer
 */
const GlobalFooter: React.FC = () => {
  const currentYear = new Date().getFullYear();

  return (
    <DefaultFooter
      copyright={`${currentYear} 宋迷茫`}
      links={[
        {
          key: 'author',
          title: (
            <>
              <UserOutlined /> 宋迷茫
            </>
          ),
          href: 'https://www.baidu.com',
          blankTarget: true
        },
        {
          key: 'github',
          title: (
            <>
              <GithubOutlined /> GitHub
            </>
          ),
          href: 'https://www.baidu.com',
          blankTarget: true
        },
        {
          key: 'feedback',
          title: (
            <>
              <BugOutlined /> 建议反馈
            </>
          ),
          href: 'https://www.baidu.com',
          blankTarget: true
        }
      ]}
    />
  );
};

export default GlobalFooter;
